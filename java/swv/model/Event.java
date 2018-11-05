/*     */ package com.atollic.truestudio.swv.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class Event
/*     */   implements Comparable<Event>
/*     */ {
/*     */   private static final String EMPTY_STRING = "";
/*     */   private static final String PIPE = " | ";
/*     */   private byte eventType;
/*  20 */   private long cycles = 0L;
/*  21 */   private double times = 0.0D;
/*  22 */   private long eventID = 0L;
/*  23 */   private long pcTimestamp = 0L;
/*     */   private boolean cyclesValueIsNotReal;
/*  25 */   private ITMLocalTimestampEvent localTimestamp = null;
/*     */ 
/*     */   public static String getEventsText(Event[] eventList)
/*     */   {
/*  31 */     String result = "";
/*  32 */     if (eventList == null) {
/*  33 */       return result;
/*     */     }
/*  35 */     if (eventList.length > 41) {
/*  36 */       return "";
/*     */     }
/*  38 */     for (int i = 1; i < eventList.length; i++) {
/*  39 */       Event event = eventList[i];
/*  40 */       if (event == null) {
/*     */         break;
/*     */       }
/*  43 */       result = result + event.getEventID() + " | " + event.printType() + " | " + event.printData();
/*  44 */       result = result + " | " + event.printCycles() + " | " + event.printTime() + " | " + event.printExtraInfo() + "\n";
/*     */     }
/*  46 */     return result;
/*     */   }
/*     */ 
/*     */   public static String getEventsText(ArrayList<Event> eventList) {
/*  50 */     String result = "";
/*  51 */     if ((eventList == null) || (eventList.isEmpty())) {
/*  52 */       return result;
/*     */     }
/*  54 */     for (int i = 1; i < eventList.size(); i++) {
/*  55 */       Event event = (Event)eventList.get(i);
/*  56 */       if (event == null) {
/*     */         break;
/*     */       }
/*  59 */       result = result + event.getEventID() + " | " + event.printType() + " | " + event.printData();
/*  60 */       result = result + " | " + event.printCycles() + " | " + event.printTime() + " | " + event.printExtraInfo() + "\n";
/*     */     }
/*  62 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean isCyclesValueIsNotReal()
/*     */   {
/*  69 */     return this.cyclesValueIsNotReal;
/*     */   }
/*     */ 
/*     */   public void setCyclesValueIsNotReal(boolean cyclesValueIsNotReal)
/*     */   {
/*  76 */     this.cyclesValueIsNotReal = cyclesValueIsNotReal;
/*     */   }
/*     */ 
/*     */   public Event() {
/*  80 */     this.pcTimestamp = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public long getCycles()
/*     */   {
/*  87 */     return this.cycles;
/*     */   }
/*     */ 
/*     */   public void setCycles(long cycles)
/*     */   {
/*  95 */     this.cycles = cycles;
/*     */   }
/*     */ 
/*     */   public double getTimes()
/*     */   {
/* 102 */     return this.times;
/*     */   }
/*     */ 
/*     */   public void setTimes(double times)
/*     */   {
/* 110 */     this.times = times;
/*     */   }
/*     */ 
/*     */   public long getEventID()
/*     */   {
/* 117 */     return this.eventID;
/*     */   }
/*     */ 
/*     */   public void setEventID(long eventID)
/*     */   {
/* 124 */     this.eventID = eventID;
/*     */   }
/*     */ 
/*     */   public void setEventType(byte eventType) {
/* 128 */     this.eventType = eventType;
/*     */   }
/*     */ 
/*     */   public byte getEventType() {
/* 132 */     return this.eventType;
/*     */   }
/*     */ 
/*     */   public long getPcTimestamp()
/*     */   {
/* 139 */     return this.pcTimestamp;
/*     */   }
/*     */ 
/*     */   public void setPcTimestamp(long pcTimestamp)
/*     */   {
/* 146 */     this.pcTimestamp = pcTimestamp;
/*     */   }
/*     */ 
/*     */   public ITMLocalTimestampEvent getLocalTimestamp() {
/* 150 */     return this.localTimestamp;
/*     */   }
/*     */ 
/*     */   public void setLocalTimestamp(ITMLocalTimestampEvent localTimestamp) {
/* 154 */     if (localTimestamp != null) {
/* 155 */       this.cycles = localTimestamp.getCycles();
/* 156 */       this.times = localTimestamp.getTimes();
/*     */     }
/* 158 */     this.localTimestamp = localTimestamp;
/*     */   }
/*     */ 
/*     */   public String printType()
/*     */   {
/* 166 */     String[] bufStrings = getClass().getName().split("\\.");
/* 167 */     if ((bufStrings == null) || (bufStrings.length < 1)) {
/* 168 */       return "";
/*     */     }
/* 170 */     return bufStrings[(bufStrings.length - 1)];
/*     */   }
/*     */ 
/*     */   public String printExtraInfo()
/*     */   {
/* 178 */     String ret = "";
/* 179 */     if (isCyclesValueIsNotReal())
/* 180 */       ret = Messages.Event_NO_TIMESTAMP_RECEIVED_CYCLE_VALUE_GUESSED + ". ";
/* 181 */     if (this.localTimestamp != null) {
/* 182 */       if ((this.localTimestamp.getTimeControl() & ITMLocalTimestampEvent.VALUE_DELAYED_REL_ASS_EV) > 0) {
/* 183 */         ret = ret + Messages.Event_TIMESTAMP_DELAYED + ". ";
/*     */       }
/* 185 */       if ((this.localTimestamp.getTimeControl() & ITMLocalTimestampEvent.VALUE_DELAYED_REL_ITM_DWT) > 0) {
/* 186 */         ret = ret + Messages.Event_PACKET_DELAYED + ". ";
/*     */       }
/*     */     }
/* 189 */     return ret;
/*     */   }
/*     */ 
/*     */   public String printTime()
/*     */   {
/* 198 */     if (isCyclesValueIsNotReal()) {
/* 199 */       return "?";
/*     */     }
/*     */ 
/* 202 */     double time = getTimes();
/*     */ 
/* 204 */     String prefix = "";
/* 205 */     if ((time < 1.0D) && (time > 0.0D)) {
/* 206 */       time *= 1000.0D;
/* 207 */       prefix = "m";
/* 208 */       if ((time < 1.0D) && (time > 0.0D)) {
/* 209 */         time *= 1000.0D;
/* 210 */         prefix = "µ";
/* 211 */         if ((time < 1.0D) && (time > 0.0D)) {
/* 212 */           time *= 1000.0D;
/* 213 */           prefix = "n";
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 218 */     return String.format("%f %ss", new Object[] { Double.valueOf(time), prefix });
/*     */   }
/*     */ 
/*     */   public String printCycles()
/*     */   {
/* 227 */     return getCycles() + (isCyclesValueIsNotReal() ? " ?" : "");
/*     */   }
/*     */ 
/*     */   public String printData()
/*     */   {
/* 235 */     return "";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 245 */     if (this == obj) {
/* 246 */       return true;
/*     */     }
/* 248 */     if (obj == null) {
/* 249 */       return false;
/*     */     }
/* 251 */     if (!(obj instanceof Event)) {
/* 252 */       return false;
/*     */     }
/* 254 */     Event other = (Event)obj;
/* 255 */     if (this.cycles != other.cycles) {
/* 256 */       return false;
/*     */     }
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   public int compareTo(Event e)
/*     */   {
/* 263 */     long ac = getCycles();
/* 264 */     long ec = e.getCycles();
/* 265 */     if (ac == ec)
/* 266 */       return 0;
/* 267 */     if (ac > ec) {
/* 268 */       return 1;
/*     */     }
/* 270 */     return -1;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.Event
 * JD-Core Version:    0.6.2
 */