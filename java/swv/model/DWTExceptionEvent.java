/*     */ package com.atollic.truestudio.swv.model;
/*     */ 
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVInterruptParser;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ 
/*     */ public class DWTExceptionEvent extends Event
/*     */ {
/*     */   public static final byte FUNC_EXCEPTION_ENTER = 1;
/*     */   public static final byte FUNC_EXCEPTION_EXIT = 2;
/*     */   public static final byte FUNC_EXCEPTION_RETURN = 3;
/*  25 */   public static final String[] EXCEPTION_NAMES = { 
/*  26 */     Messages.MODEL_NA, 
/*  27 */     Messages.DWTExceptionEvent_RESET, 
/*  28 */     Messages.DWTExceptionEvent_NMI, 
/*  29 */     Messages.DWTExceptionEvent_HARD_FAULT, 
/*  30 */     Messages.DWTExceptionEvent_MEMORY_MANAGE_FAULT, 
/*  31 */     Messages.DWTExceptionEvent_BUS_FAULT, 
/*  32 */     Messages.DWTExceptionEvent_USAGE_FAULT, 
/*  33 */     Messages.DWTExceptionEvent_RESERVED, 
/*  34 */     Messages.DWTExceptionEvent_RESERVED, 
/*  35 */     Messages.DWTExceptionEvent_RESERVED, 
/*  36 */     Messages.DWTExceptionEvent_RESERVED, 
/*  37 */     Messages.DWTExceptionEvent_SVCALL, 
/*  38 */     Messages.DWTExceptionEvent_DEBUG_MONITOR, 
/*  39 */     Messages.DWTExceptionEvent_RESERVED, 
/*  40 */     Messages.DWTExceptionEvent_PENDSV, 
/*  41 */     Messages.DWTExceptionEvent_SYSTICK };
/*     */   private int exceptionNumber;
/*     */   private byte function;
/*  46 */   private String name = null;
/*  47 */   private String functionAddress = null;
/*  48 */   private String functionCall = null;
/*  49 */   private String peripheral = null;
/*  50 */   private SWVInterruptParser parser = null;
/*  51 */   private boolean includedInStatistics = false;
/*     */ 
/*     */   public DWTExceptionEvent(int exceptionNumber, byte function) {
/*  54 */     this.exceptionNumber = exceptionNumber;
/*  55 */     this.function = function;
/*     */   }
/*     */ 
/*     */   public int getExceptionNumber()
/*     */   {
/*  62 */     return this.exceptionNumber;
/*     */   }
/*     */ 
/*     */   public void setExceptionNumber(int exceptionNumber)
/*     */   {
/*  68 */     this.exceptionNumber = exceptionNumber;
/*     */   }
/*     */ 
/*     */   public byte getFunction()
/*     */   {
/*  74 */     return this.function;
/*     */   }
/*     */ 
/*     */   public void setFunction(byte function)
/*     */   {
/*  80 */     this.function = function;
/*     */   }
/*     */ 
/*     */   private String getName()
/*     */   {
/*  87 */     return this.name;
/*     */   }
/*     */ 
/*     */   private void setName(String name)
/*     */   {
/*  94 */     this.name = name;
/*     */   }
/*     */ 
/*     */   private String getFunctionAddress()
/*     */   {
/* 101 */     return this.functionAddress;
/*     */   }
/*     */ 
/*     */   private void setFunctionAddress(String functionAddress)
/*     */   {
/* 108 */     this.functionAddress = functionAddress;
/*     */   }
/*     */ 
/*     */   private String getFunctionCall()
/*     */   {
/* 115 */     return this.functionCall;
/*     */   }
/*     */ 
/*     */   private void setFunctionCall(String functionCall)
/*     */   {
/* 122 */     this.functionCall = functionCall;
/*     */   }
/*     */ 
/*     */   private String getPeripheral()
/*     */   {
/* 129 */     return this.peripheral;
/*     */   }
/*     */ 
/*     */   private void setPeripheral(String peripheral)
/*     */   {
/* 136 */     this.peripheral = peripheral;
/*     */   }
/*     */ 
/*     */   public String printType()
/*     */   {
/* 141 */     String func = Messages.MODEL_UNKNOWN;
/* 142 */     switch (this.function) {
/*     */     case 1:
/* 144 */       func = Messages.DWTExceptionEvent_ENTRY;
/* 145 */       break;
/*     */     case 2:
/* 147 */       func = Messages.DWTExceptionEvent_EXIT;
/* 148 */       break;
/*     */     case 3:
/* 150 */       func = Messages.DWTExceptionEvent_RETURN;
/* 151 */       break;
/*     */     default:
/* 153 */       func = Messages.MODEL_UNKNOWN;
/*     */     }
/*     */ 
/* 156 */     return Messages.DWTExceptionEvent_EXCEPTION + func;
/*     */   }
/*     */ 
/*     */   public String printData()
/*     */   {
/* 164 */     if (getName() != null) {
/* 165 */       return getName();
/*     */     }
/* 167 */     String exception = "";
/* 168 */     int num = getExceptionNumber();
/* 169 */     setParser();
/* 170 */     if (num >= 0) {
/* 171 */       if (this.parser != null) {
/* 172 */         exception = this.parser.getInterruptName(num);
/*     */       }
/* 174 */       if ((exception == null) || (exception.isEmpty())) {
/* 175 */         exception = String.valueOf(num);
/*     */       }
/* 177 */       setName(exception);
/*     */     }
/* 179 */     if (getName() == null) {
/* 180 */       setName(String.valueOf(num));
/*     */     }
/* 182 */     return getName();
/*     */   }
/*     */ 
/*     */   public String printPeripheral()
/*     */   {
/* 189 */     if (getPeripheral() != null) {
/* 190 */       return getPeripheral();
/*     */     }
/* 192 */     String peripheral = "";
/* 193 */     int num = getExceptionNumber();
/* 194 */     setParser();
/* 195 */     if (num >= 0) {
/* 196 */       if (this.parser != null) {
/* 197 */         peripheral = this.parser.getInterruptPeripheral(num);
/*     */       }
/* 199 */       setPeripheral(peripheral);
/*     */     }
/* 201 */     if (getPeripheral() == null) {
/* 202 */       setPeripheral("");
/*     */     }
/* 204 */     return getPeripheral();
/*     */   }
/*     */ 
/*     */   public String printFunctionAddress() {
/* 208 */     if (getFunctionAddress() != null) {
/* 209 */       return getFunctionAddress();
/*     */     }
/* 211 */     String functionAddress = "";
/* 212 */     int num = getExceptionNumber();
/* 213 */     setParser();
/* 214 */     if ((num < 0) || (this.parser == null) || (!this.parser.interruptHasAllInfo(num))) {
/* 215 */       return "-1";
/*     */     }
/* 217 */     functionAddress = this.parser.getInterruptFunctionAddress(num);
/* 218 */     if ((functionAddress == null) || (functionAddress.isEmpty()) || (functionAddress.equals("0"))) {
/* 219 */       functionAddress = "-1";
/*     */     }
/* 221 */     setFunctionAddress(functionAddress);
/* 222 */     return getFunctionAddress();
/*     */   }
/*     */ 
/*     */   public String printFunc()
/*     */   {
/* 227 */     if (getFunctionCall() != null) {
/* 228 */       return getFunctionCall();
/*     */     }
/* 230 */     String functionName = "";
/* 231 */     int num = getExceptionNumber();
/* 232 */     setParser();
/* 233 */     if ((num < 0) || (this.parser == null) || (!this.parser.interruptHasAllInfo(num))) {
/* 234 */       return "";
/*     */     }
/* 236 */     functionName = this.parser.getInterruptFunction(num);
/* 237 */     if ((functionName == null) || (functionName.isEmpty())) {
/* 238 */       functionName = Messages.MODEL_NA;
/*     */     }
/* 240 */     setFunctionCall(functionName);
/* 241 */     return getFunctionCall();
/*     */   }
/*     */ 
/*     */   private void setParser()
/*     */   {
/* 249 */     if (this.parser != null)
/* 250 */       return;
/* 251 */     SWVClient currentSWVclient = SWVPlugin.getDefault().getSessionManager().getClient();
/* 252 */     if (currentSWVclient == null)
/* 253 */       return;
/* 254 */     if (!currentSWVclient.isParsingInterrupts())
/* 255 */       this.parser = currentSWVclient.startParsingInterrupts();
/* 256 */     else if (this.parser == null)
/* 257 */       this.parser = currentSWVclient.getInterruptParser();
/*     */   }
/*     */ 
/*     */   public void setIncludedInStatistics()
/*     */   {
/* 262 */     this.includedInStatistics = true;
/*     */   }
/*     */ 
/*     */   public boolean isIncludedInStatistics() {
/* 266 */     return this.includedInStatistics;
/*     */   }
/*     */ 
/*     */   public boolean isEntryEvent() {
/* 270 */     return this.function == 1;
/*     */   }
/*     */ 
/*     */   public boolean isExitEvent() {
/* 274 */     return this.function == 2;
/*     */   }
/*     */ 
/*     */   public boolean isReturnEvent() {
/* 278 */     return this.function == 3;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.DWTExceptionEvent
 * JD-Core Version:    0.6.2
 */