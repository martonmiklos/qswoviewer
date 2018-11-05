/*     */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*     */ 
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ 
/*     */ public class SWVDataTraceUtil
/*     */ {
/*     */   private static final String EMPTY_STRING = "";
/*     */   public static final byte COMPARATOR_MAX = 4;
/*     */ 
/*     */   public static String getComparatorAddress(byte cmpId, ILaunchConfiguration config)
/*     */   {
/*  26 */     return getComparatorParam("Address", cmpId, config);
/*     */   }
/*     */ 
/*     */   public static String getComparatorAccess(byte cmpId, ILaunchConfiguration config)
/*     */   {
/*  36 */     return getComparatorParam("Access", cmpId, config);
/*     */   }
/*     */ 
/*     */   public static String getComparatorSize(byte cmpId, ILaunchConfiguration config)
/*     */   {
/*  46 */     return getComparatorParam("Size", cmpId, config);
/*     */   }
/*     */ 
/*     */   public static String getComparatorFunction(byte cmpId, ILaunchConfiguration config)
/*     */   {
/*  56 */     return getComparatorParam("Function", cmpId, config);
/*     */   }
/*     */ 
/*     */   public static boolean[] getEnabledComparaters(ILaunchConfiguration config)
/*     */   {
/*  66 */     boolean[] isEnabled = new boolean[4];
/*  67 */     for (byte cmpId = 0; cmpId < 4; cmpId = (byte)(cmpId + 1)) {
/*  68 */       isEnabled[cmpId] = (config == null ? 0 : getComparatorEnabled(cmpId, config));
/*     */     }
/*  70 */     return isEnabled;
/*     */   }
/*     */ 
/*     */   public static boolean getComparatorEnabled(byte cmpId, ILaunchConfiguration config)
/*     */   {
/*  80 */     String enabled = getComparatorParam("Enabled", cmpId, config);
/*  81 */     return enabled == null ? false : enabled.equals("true");
/*     */   }
/*     */ 
/*     */   private static String getComparatorParam(String param, byte cmpId, ILaunchConfiguration config)
/*     */   {
/*  86 */     if (config != null) {
/*  87 */       String attrStr = "";
/*     */       try
/*     */       {
/*  90 */         attrStr = config.getAttribute("com.atollic.truestudio.swv.core.datatrace_" + cmpId, "");
/*     */       } catch (CoreException e) {
/*  92 */         return null;
/*     */       }
/*  94 */       String[] keyValStrs = attrStr.split(":");
/*     */ 
/*  96 */       for (String keyValStr : keyValStrs) {
/*  97 */         String[] keyVal = keyValStr.split("=");
/*     */ 
/*  99 */         if (keyVal.length == 2) {
/* 100 */           String id = keyVal[0];
/* 101 */           String val = keyVal[1];
/* 102 */           if (id.equals(param)) {
/* 103 */             return val;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public static String convert(long dataValue, String type, int valueFormat)
/*     */   {
/* 118 */     String result = "";
/*     */ 
/* 120 */     if ((type != null) && (type.contains("*")))
/*     */     {
/* 122 */       result = "0x" + Long.toHexString(dataValue);
/* 123 */     } else if (valueFormat == 5) {
/* 124 */       if (type != null) {
/* 125 */         String noneVolatileType = type.replace("volatile ", "");
/* 126 */         type = noneVolatileType.replace("const ", "");
/* 127 */         if ((type.equals("long")) || (type.equals("int")) || (type.equals("long int")) || (type.equals("int32_t")))
/*     */         {
/* 129 */           result = (int)dataValue;
/* 130 */         } else if ((type.equals("short")) || (type.equals("short int")) || (type.equals("int16_t")))
/*     */         {
/* 132 */           result = (short)(int)dataValue;
/* 133 */         } else if (type.equals("signed char"))
/* 134 */           result = (byte)(int)dataValue;
/* 135 */         else if (type.equals("float"))
/* 136 */           result = Float.intBitsToFloat((int)dataValue);
/*     */         else
/* 138 */           result = dataValue;
/*     */       }
/*     */       else {
/* 141 */         result = dataValue;
/*     */       }
/* 143 */     } else if (valueFormat == 0)
/* 144 */       result = "0x" + Long.toHexString(dataValue);
/* 145 */     else if (valueFormat == 2) {
/* 146 */       result = Long.toBinaryString(dataValue);
/*     */     }
/*     */ 
/* 149 */     return result;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceUtil
 * JD-Core Version:    0.6.2
 */