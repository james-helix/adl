{-# LANGUAGE OverloadedStrings #-}
module ADL.Test(
    S(..),
) where

import ADL.Core
import Control.Applicative( (<$>), (<*>), (<|>) )
import qualified ADL.Sys.Types
import qualified Data.Aeson as JS
import qualified Data.HashMap.Strict as HM
import qualified Data.Int
import qualified Data.Map as M
import qualified Data.Proxy
import qualified Data.Text as T
import qualified Prelude

data S = S
    { s_f_pair :: (ADL.Sys.Types.Pair Data.Int.Int32 Prelude.Double)
    , s_f_either :: (ADL.Sys.Types.Either T.Text Data.Int.Int32)
    , s_f_error :: (ADL.Sys.Types.Error Data.Int.Int32)
    , s_f_map :: (ADL.Sys.Types.Map T.Text Prelude.Double)
    , s_f_set :: (ADL.Sys.Types.Set T.Text)
    , s_f_mstring :: (ADL.Sys.Types.Maybe T.Text)
    , s_f_mstring2 :: (ADL.Sys.Types.Maybe T.Text)
    , s_f_nstring :: Maybe (T.Text)
    , s_f_nstring2 :: Maybe (T.Text)
    }
    deriving (Prelude.Eq,Prelude.Ord,Prelude.Show)

mkS :: (ADL.Sys.Types.Pair Data.Int.Int32 Prelude.Double) -> (ADL.Sys.Types.Either T.Text Data.Int.Int32) -> (ADL.Sys.Types.Error Data.Int.Int32) -> (ADL.Sys.Types.Map T.Text Prelude.Double) -> (ADL.Sys.Types.Set T.Text) -> (ADL.Sys.Types.Maybe T.Text) -> Maybe (T.Text) -> S
mkS f_pair f_either f_error f_map f_set f_mstring f_nstring = S f_pair f_either f_error f_map f_set f_mstring (Prelude.Just "sukpeepolup") f_nstring (Prelude.Just "abcde")

instance AdlValue S where
    atype _ = "test.S"
    
    jsonGen = genObject
        [ genField "f_pair" s_f_pair
        , genField "f_either" s_f_either
        , genField "f_error" s_f_error
        , genField "f_map" s_f_map
        , genField "f_set" s_f_set
        , genField "f_mstring" s_f_mstring
        , genField "f_mstring2" s_f_mstring2
        , genField "f_nstring" s_f_nstring
        , genField "f_nstring2" s_f_nstring2
        ]
    
    jsonParser = S
        <$> parseField "f_pair"
        <*> parseField "f_either"
        <*> parseField "f_error"
        <*> parseField "f_map"
        <*> parseField "f_set"
        <*> parseField "f_mstring"
        <*> parseFieldDef "f_mstring2" (Prelude.Just "sukpeepolup")
        <*> parseField "f_nstring"
        <*> parseFieldDef "f_nstring2" (Prelude.Just "abcde")