package tlo.regex;

public class DotRegex implements Regex {
  @Override
  public String toString() {
    return "Dot";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public boolean match(String string) {
    return string.length() == 1;
  }

  @Override
  public String unparse() {
    return ".";
  }
}
