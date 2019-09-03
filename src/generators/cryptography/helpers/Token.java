package generators.cryptography.helpers;

public class Token {

  private String  sign     = null;
  private Double  num      = null;

  private boolean isOpen   = false;
  private boolean isClose  = false;
  private boolean isDouble = false;
  private boolean isNum    = false;
  private boolean isSign   = false;

  public Token(char sign) {
    if (sign == '(') {
      this.sign = "(";
      isOpen = true;
    } else if (sign == ')') {
      this.sign = ")";
      isClose = true;
    }

  }

  public Token(String sign, boolean isNum) {
    if (isNum) {
      if (sign.contains("."))
        isDouble = true;
      num = Double.parseDouble(sign);
      this.sign = sign;
      this.isNum = true;
    } else {
      this.sign = sign;
      isSign = true;
    }
  }

  public boolean isOpen() {
    return isOpen;
  }

  public boolean isClose() {
    return isClose;
  }

  public boolean isDouble() {
    return isDouble;
  }

  public boolean isSign() {
    return isSign;
  }

  public String getSign() {
    return sign;
  }

  public Double getNum() {
    return num;
  }

  public boolean isNum() {
    return isNum;
  }
}
