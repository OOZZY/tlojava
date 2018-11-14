package tlo.regex;

public abstract class AbstractRecursiveRegex implements Regex {
  protected Regex regex;

  public AbstractRecursiveRegex(Regex regex) {
    this.regex = regex;
  }

  public Regex getRegex() {
    return regex;
  }

  public void setRegex(Regex regex) {
    this.regex = regex;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((regex == null) ? 0 : regex.hashCode());
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
    AbstractRecursiveRegex other = (AbstractRecursiveRegex) obj;
    if (regex == null) {
      if (other.regex != null) {
        return false;
      }
    } else if (!regex.equals(other.regex)) {
      return false;
    }
    return true;
  }
}
