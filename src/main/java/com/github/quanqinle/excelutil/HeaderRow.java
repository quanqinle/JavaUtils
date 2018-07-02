/**
 * 
 */
package com.github.quanqinle.excelutil;

/**
 * @author quanql
 *
 */
public class HeaderRow {
  
  private int index;
  
  /**
   * 表头层级
   */
  private int level = 1;

  /**
   * 是否有隐藏行
   */
  private boolean hasHide = false;
  /**
   * 是否有隐藏行
   */
  public boolean hasHide() {
    return hasHide;
  }
  
  /**
   * 
   */
  public HeaderRow() {
    // TODO Auto-generated constructor stub
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
