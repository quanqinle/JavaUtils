package com.github.quanqinle.util.excelutil.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;

import com.github.quanqinle.util.LogUtil;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelField {

  int index();

  ExcelFieldType type() default ExcelFieldType.Str;

  enum ExcelFieldType {

    Str {
      @Override
      public String buildSetString(String in) {
        return in;
      }
    },
    Date {
      @Override
      public String buildSetString(String in) {

        if (in.matches("\\d{4}-\\d{2}-\\d{2}")) {
          return in;
        }
        if (in.matches("\\d{4}/\\d{2}/\\d{2}")) {
          return in.replaceAll("/", "-");
        }
        if (in.matches("\\d{1,2}/\\d{1,2}/\\d{2}")) {
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
          SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
          try {
            return simpleDateFormat2.format(simpleDateFormat.parse(in));
          } catch (Exception e) {
            LogUtil.error(String.format("未知的日期格式,日期为:%s", in));
          }
        }
        LogUtil.error(String.format("未知的日期格式,日期为:%s", in));
        return in;
      }
    };

    public abstract String buildSetString(String str);
  }
}
