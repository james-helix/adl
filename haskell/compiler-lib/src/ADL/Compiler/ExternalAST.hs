{-# LANGUAGE OverloadedStrings #-}
module ADL.Compiler.ExternalAST(
  moduleToA2
  ) where

import qualified Data.Map as Map
import qualified Data.Text as T
import qualified Data.ByteString.Lazy as LBS
import qualified Data.Aeson as JSON
import qualified Data.Vector as V
import qualified Data.HashMap.Strict as HM
import qualified Data.Scientific as S
import Data.Text.Lazy.Builder

import Control.Monad
import Control.Monad.Trans

import ADL.Compiler.EIO
import ADL.Compiler.AST
import ADL.Compiler.Processing
import ADL.Compiler.Primitive
import ADL.Compiler.Utils

import ADL.Core.Value

import qualified ADL.Sys.Adlast as A2

-- | Convert the internal AST into an external one, suitable for serialisation.

moduleToA2 :: RModule -> A2.Module
moduleToA2 m = A2.Module name imports decls
  where
    name = moduleNameToA2 (m_name m)
    imports = map importToA2 (m_imports m)
    decls = Map.map declToA2 (m_decls m)

importToA2 :: Import -> A2.Import
importToA2 (Import_Module mname) = A2.Import_moduleName (moduleNameToA2 mname)
importToA2 (Import_ScopedName sn) = A2.Import_scopedName (scopedNameToA2 sn)

moduleNameToA2 :: ModuleName -> A2.ModuleName
moduleNameToA2 mn = T.intercalate "." (unModuleName mn)

scopedNameToA2 :: ScopedName -> A2.ScopedName
scopedNameToA2 sn =A2.ScopedName (moduleNameToA2 (sn_moduleName sn))  (sn_name sn)

declToA2 :: RDecl -> A2.Decl
declToA2 d = A2.Decl (d_name d) (fmap fromIntegral (d_version d)) (declTypeToA2 (d_type d)) (annotationsToA2 (d_annotations d))

declTypeToA2 :: DeclType ResolvedType -> A2.DeclType
declTypeToA2 (Decl_Struct s) = A2.DeclType_struct_ (A2.Struct (s_typeParams s) (map fieldToA2 (s_fields s)))
declTypeToA2 (Decl_Union u) = A2.DeclType_union_ (A2.Union (u_typeParams u) (map fieldToA2 (u_fields u)))
declTypeToA2 (Decl_Typedef t) = A2.DeclType_type_ (A2.TypeDef (t_typeParams t) (typeExprToA2 (t_typeExpr t)))
declTypeToA2 (Decl_Newtype n) = A2.DeclType_newtype_ (A2.NewType (n_typeParams n) (typeExprToA2 (n_typeExpr n)) (fmap literalToA2 (n_default n)))

fieldToA2 :: Field ResolvedType -> A2.Field
fieldToA2 f = A2.Field (f_name f) (f_serializedName f) (typeExprToA2 (f_type f)) (fmap literalToA2 (f_default f)) (annotationsToA2 (f_annotations f))

typeExprToA2 :: TypeExpr ResolvedType -> A2.TypeExpr
typeExprToA2 (TypeExpr rt rts) = A2.TypeExpr (typeRefToA2 rt) (map typeExprToA2 rts)

typeRefToA2 :: ResolvedType -> A2.TypeRef
typeRefToA2 (RT_Named (sn,_)) = A2.TypeRef_reference (scopedNameToA2 sn)
typeRefToA2 (RT_Param i) = A2.TypeRef_typeParam i
typeRefToA2 (RT_Primitive p) = A2.TypeRef_primitive (ptToText p)

literalToA2 :: JSON.Value -> A2.Literal
literalToA2 JSON.Null = A2.Literal_null
literalToA2 (JSON.Number n) = case S.floatingOrInteger n of
     Left d -> A2.Literal_double d
     Right i -> A2.Literal_integer i
literalToA2 (JSON.String s) = A2.Literal_string s
literalToA2 (JSON.Bool b) = A2.Literal_boolean b
literalToA2 (JSON.Array vs) = A2.Literal_array (fmap literalToA2 (V.toList vs))
literalToA2 (JSON.Object hm) = A2.Literal_object (Map.fromList (fmap (\(k,v) -> (k,literalToA2 v)) (HM.toList hm)))

annotationsToA2 :: Map.Map ScopedName (ResolvedType,JSON.Value) -> Map.Map A2.ScopedName A2.Literal
annotationsToA2 = Map.fromList . map (\(k,(_,v)) -> (scopedNameToA2 k,literalToA2 v)) . Map.toList
