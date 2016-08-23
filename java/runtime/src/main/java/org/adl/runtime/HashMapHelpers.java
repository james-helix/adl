package org.adl.runtime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.adl.sys.types.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HashMapHelpers
{
  public static <K,V> Factory<HashMap<K,V>> factory(final Factory<K> keyFactory,final Factory <V> valueFactory) {
    return new Factory<HashMap<K,V>>() {
      @Override
      public HashMap<K,V> create() {
        return new HashMap<>();
      }

      @Override
      public HashMap<K,V> create(HashMap<K,V> other) {
        HashMap<K,V> result = new HashMap<K,V>();
        for (Map.Entry<K,V> e : other.entrySet()) {
          result.put(keyFactory.create(e.getKey()), valueFactory.create(e.getValue()));
        }
        return result;
      }
    };
  }

  public static <K,V> HashMap<K,V> create(ArrayList<Pair<K,V>> vals) {
    HashMap<K,V> result = new HashMap<K,V>();
    for (Pair<K,V> p : vals) {
      result.put(p.getV1(),p.getV2());
    }
    return result;
  }

  public static <K,V> JsonBinding<HashMap<K,V>> jsonBinding(
      final JsonBinding<K> bindingK,final JsonBinding<V> bindingV) {
    final Factory<HashMap<K,V>> _factory = factory(bindingK.factory(), bindingV.factory());

    return new JsonBinding<HashMap<K,V>>() {
      public Factory<HashMap<K,V>> factory() {
        return _factory;
      };

      public JsonElement toJson(HashMap<K,V> value) {
        JsonArray result = new JsonArray();
        for (Map.Entry<K,V> entry : value.entrySet()) {
          JsonObject pair = new JsonObject();
          pair.add("v1", bindingK.toJson(entry.getKey()));
          pair.add("v2", bindingV.toJson(entry.getValue()));
          result.add(pair);
        }
        return result;
      }

      public HashMap<K,V> fromJson(JsonElement json) {
        JsonArray array = json.getAsJsonArray();
        HashMap<K,V> result = new HashMap<>();
        for(int i = 0; i < array.size(); i++) {
          JsonObject pair = array.get(i).getAsJsonObject();
          if(!pair.has("v1") || !pair.has("v2"))
            throw new IllegalStateException();
          result.put(bindingK.fromJson(pair.get("v1")), bindingV.fromJson(pair.get("v2")));
        }
        return result;
      }
    };
  }
};