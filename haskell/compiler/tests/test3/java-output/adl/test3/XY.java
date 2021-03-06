/* Code generated from adl module test3 */

package adl.test3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.adl.runtime.Factory;
import org.adl.runtime.JsonBinding;
import org.adl.runtime.JsonBindings;
import org.adl.runtime.Lazy;
import java.util.Objects;

public class XY<T> {

  /* Members */

  private T x;
  private T y;

  /* Constructors */

  public XY(T x, T y) {
    this.x = Objects.requireNonNull(x);
    this.y = Objects.requireNonNull(y);
  }

  /* Accessors and mutators */

  public T getX() {
    return x;
  }

  public void setX(T x) {
    this.x = Objects.requireNonNull(x);
  }

  public T getY() {
    return y;
  }

  public void setY(T y) {
    this.y = Objects.requireNonNull(y);
  }

  /* Object level helpers */

  @Override
  public boolean equals(Object other0) {
    if (!(other0 instanceof XY)) {
      return false;
    }
    XY other = (XY) other0;
    return
      x.equals(other.x) &&
      y.equals(other.y);
  }

  @Override
  public int hashCode() {
    int _result = 1;
    _result = _result * 37 + x.hashCode();
    _result = _result * 37 + y.hashCode();
    return _result;
  }

  /* Factory for construction of generic values */

  public static <T> Factory<XY<T>> factory(Factory<T> factoryT) {
    return new Factory<XY<T>>() {
      final Lazy<Factory<T>> x = new Lazy<>(() -> factoryT);
      final Lazy<Factory<T>> y = new Lazy<>(() -> factoryT);

      public XY<T> create() {
        return new XY<T>(
          x.get().create(),
          y.get().create()
          );
      }

      public XY<T> create(XY<T> other) {
        return new XY<T>(
          x.get().create(other.getX()),
          y.get().create(other.getY())
          );
      }
    };
  }

  /* Json serialization */

  public static<T> JsonBinding<XY<T>> jsonBinding(JsonBinding<T> bindingT) {
    final Lazy<JsonBinding<T>> x = new Lazy<>(() -> bindingT);
    final Lazy<JsonBinding<T>> y = new Lazy<>(() -> bindingT);
    final Factory<T> factoryT = bindingT.factory();
    final Factory<XY<T>> _factory = factory(bindingT.factory());

    return new JsonBinding<XY<T>>() {
      public Factory<XY<T>> factory() {
        return _factory;
      }

      public JsonElement toJson(XY<T> _value) {
        JsonObject _result = new JsonObject();
        _result.add("x", x.get().toJson(_value.x));
        _result.add("y", y.get().toJson(_value.y));
        return _result;
      }

      public XY<T> fromJson(JsonElement _json) {
        JsonObject _obj = JsonBindings.objectFromJson(_json);
        return new XY<T>(
          JsonBindings.fieldFromJson(_obj, "x", x.get()),
          JsonBindings.fieldFromJson(_obj, "y", y.get())
        );
      }
    };
  }
}
