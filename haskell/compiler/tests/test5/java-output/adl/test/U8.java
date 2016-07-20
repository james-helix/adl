package adl.test;

import org.adl.runtime.Factories;
import org.adl.runtime.Factory;

public class U8 {

  private Disc disc;
  private Object value;

  public enum Disc {
    V1,
    V2
  }

  public static U8 v1(S1 v) {
    return new U8(Disc.V1,java.util.Objects.requireNonNull(v));
  }

  public static U8 v2(short v) {
    return new U8(Disc.V2,v);
  }

  public U8() {
    this.disc = Disc.V1;
    this.value = new S1();
  }

  public U8(U8 other) {
    this.disc = other.disc;
    switch (other.disc) {
      case V1:
        this.value = S1.factory.create((S1) other.value);
        break;
      case V2:
        this.value = (Short) other.value;
        break;
    }
  }

  private U8(Disc disc, Object value) {
    this.disc = disc;
    this.value = value;
  }

  public Disc getDisc() {
    return disc;
  }

  public S1 getV1() {
    if (disc == Disc.V1) {
      return cast(value);
    }
    throw new IllegalStateException();
  }

  public short getV2() {
    if (disc == Disc.V2) {
      return cast(value);
    }
    throw new IllegalStateException();
  }

  public void setV1(S1 v) {
    this.value = java.util.Objects.requireNonNull(v);
    this.disc = Disc.V1;
  }

  public void setV2(short v) {
    this.value = v;
    this.disc = Disc.V2;
  }

  public boolean equals(U8 other) {
    return disc == other.disc && value.equals(other.value);
  }

  public int hashCode() {
    return disc.hashCode() * 37 + value.hashCode();
  }

  @SuppressWarnings("unchecked")
  private static <T> T cast(final Object o) {
    return (T)o;
  }

  public static Factory<U8> factory = new Factory<U8>() {
    public U8 create() {
      return new U8();
    }
    public U8 create(U8 other) {
      return new U8(other);
    }
  };
}