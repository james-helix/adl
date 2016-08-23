package adl.test2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.adl.runtime.Factories;
import org.adl.runtime.Factory;
import org.adl.runtime.JsonBinding;
import org.adl.runtime.JsonBindings;
import java.util.Objects;

public class A {

  /* Members */

  private int a;

  /* Constructors */

  public A(int a) {
    this.a = a;
  }

  public A() {
    this.a = 0;
  }

  public A(A other) {
    this.a = other.a;
  }

  /* Accessors and mutators */

  public int getA() {
    return a;
  }

  public void setA(int a) {
    this.a = a;
  }

  /* Object level helpers */

  @Override
  public boolean equals(Object other0) {
    if (!(other0 instanceof A)) {
      return false;
    }
    A other = (A) other0;
    return
      a == other.a;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = result * 37 + a;
    return result;
  }

  /* Factory for construction of generic values */

  public static final Factory<A> FACTORY = new Factory<A>() {
    public A create() {
      return new A();
    }
    public A create(A other) {
      return new A(other);
    }
  };

  /* Json serialization */

  public static JsonBinding<A> jsonBinding() {
    final JsonBinding<Integer> a = JsonBindings.INTEGER;
    final Factory<A> _factory = FACTORY;

    return new JsonBinding<A>() {
      public Factory<A> factory() {
        return _factory;
      }

      public JsonElement toJson(A _value) {
        JsonObject _result = new JsonObject();
        _result.add("a", a.toJson(_value.a));
        return _result;
      }

      public A fromJson(JsonElement _json) {
        JsonObject _obj = _json.getAsJsonObject();
        return new A(
          _obj.has("a") ? a.fromJson(_obj.get("a")) : 0
        );
      }
    };
  }
}