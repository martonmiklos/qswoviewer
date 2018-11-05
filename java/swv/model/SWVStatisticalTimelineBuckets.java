/*     */ package com.atollic.truestudio.swv.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class SWVStatisticalTimelineBuckets
/*     */ {
/*     */   public static final int EVENT_QUEUE_LENGTH = 40;
/*     */   private ArrayList<Double> x_values;
/*     */   private ArrayList<Double> y_values;
/*     */   private ArrayList<ConcurrentHashMap<String, Integer>> spanEventStatistics;
/*     */   public ArrayList<Event[]> eventSerie;
/*     */   private Class eventClass;
/*  20 */   private double y_max = 0.0D;
/*  21 */   private double x_min = 1000000000000.0D;
/*     */   private double x_max;
/*     */   private int SWVCoreClock;
/*     */ 
/*     */   public SWVStatisticalTimelineBuckets(int cloc_speed, Class theEventClas)
/*     */   {
/*  29 */     this.x_values = new ArrayList(400);
/*  30 */     this.y_values = new ArrayList(400);
/*  31 */     this.spanEventStatistics = new ArrayList(400);
/*  32 */     this.eventSerie = new ArrayList(400);
/*  33 */     this.SWVCoreClock = cloc_speed;
/*     */ 
/*  35 */     this.eventClass = theEventClas;
/*     */   }
/*     */ 
/*     */   public double[] getLatestValueSet() {
/*  39 */     double[] d = new double[2];
/*  40 */     d[0] = ((Double)this.x_values.get(this.x_values.size() - 1)).doubleValue();
/*  41 */     d[1] = ((Double)this.y_values.get(this.y_values.size() - 1)).doubleValue();
/*  42 */     return d;
/*     */   }
/*     */ 
/*     */   public void addValueSet(double xvalue, double yvalue)
/*     */   {
/*  49 */     this.x_values.add(Double.valueOf(xvalue));
/*  50 */     this.y_values.add(Double.valueOf(yvalue));
/*  51 */     if (this.x_max < xvalue) {
/*  52 */       this.x_max = xvalue;
/*     */     }
/*  54 */     if (this.x_min > xvalue)
/*  55 */       this.x_min = xvalue;
/*     */   }
/*     */ 
/*     */   public void addValueSet(double xvalue, double yvalue, ConcurrentHashMap<String, Integer> eventStatistics)
/*     */   {
/*  60 */     addValueSet(xvalue, yvalue);
/*  61 */     this.spanEventStatistics.add(eventStatistics);
/*     */   }
/*     */ 
/*     */   public void addNewFirstValueset()
/*     */   {
/*  66 */     ConcurrentHashMap eventStatistics = new ConcurrentHashMap(1);
/*  67 */     this.x_values.add(0, Double.valueOf(0.0D));
/*  68 */     this.y_values.add(0, Double.valueOf(0.0D));
/*  69 */     this.x_min = 0.0D;
/*  70 */     this.spanEventStatistics.add(0, eventStatistics);
/*  71 */     this.eventSerie.add(0, new Event[40]);
/*     */   }
/*     */ 
/*     */   public ArrayList<Double> getXValueSet() {
/*  75 */     return this.x_values;
/*     */   }
/*     */ 
/*     */   public ArrayList<Double> getYValueSet() {
/*  79 */     return this.y_values;
/*     */   }
/*     */ 
/*     */   public ArrayList<Event[]> getToolTipEvents() {
/*  83 */     return this.eventSerie;
/*     */   }
/*     */ 
/*     */   public ArrayList<ConcurrentHashMap<String, Integer>> getSpanEventStatisticsSet()
/*     */   {
/*  89 */     return this.spanEventStatistics;
/*     */   }
/*     */ 
/*     */   public double[] getXarray(int swv_scale_type) {
/*  93 */     double[] serie = new double[getXValueSet().size()];
/*  94 */     Double[] doubles = new Double[getXValueSet().size()];
/*  95 */     getXValueSet().toArray(doubles);
/*  96 */     for (int i = 0; i < doubles.length; i++) {
/*  97 */       if (swv_scale_type == 0)
/*  98 */         serie[i] = doubles[i].doubleValue();
/*     */       else {
/* 100 */         serie[i] = (doubles[i].doubleValue() / getSWVCoreClock());
/*     */       }
/*     */     }
/* 103 */     return serie;
/*     */   }
/*     */ 
/*     */   public double[] getYarray()
/*     */   {
/* 111 */     double[] serie = new double[getYValueSet().size()];
/* 112 */     Double[] doubles = new Double[getYValueSet().size()];
/* 113 */     getYValueSet().toArray(doubles);
/* 114 */     for (int i = 0; i < doubles.length; i++) {
/* 115 */       serie[i] = doubles[i].doubleValue();
/* 116 */       if (getYMax() < serie[i]) {
/* 117 */         setYMax(serie[i]);
/*     */       }
/*     */     }
/* 120 */     return serie;
/*     */   }
/*     */ 
/*     */   public void setXMin(double min) {
/* 124 */     if (min < this.x_min)
/* 125 */       this.x_min = min;
/*     */   }
/*     */ 
/*     */   public double getXMin(int swvScaleType)
/*     */   {
/* 132 */     if (swvScaleType == 0) {
/* 133 */       return this.x_min;
/*     */     }
/* 135 */     return this.x_min / getSWVCoreClock();
/*     */   }
/*     */ 
/*     */   public void setXMax(double max) {
/* 139 */     this.x_max = max;
/*     */   }
/*     */ 
/*     */   public double getXMax(int swvScaleType)
/*     */   {
/* 146 */     if (swvScaleType == 0) {
/* 147 */       return this.x_max;
/*     */     }
/* 149 */     return this.x_max / getSWVCoreClock();
/*     */   }
/*     */ 
/*     */   public void setYMax(double max) {
/* 153 */     this.y_max = max;
/*     */   }
/*     */ 
/*     */   public double getYMax() {
/* 157 */     return this.y_max;
/*     */   }
/*     */ 
/*     */   public int getSWVCoreClock() {
/* 161 */     return this.SWVCoreClock;
/*     */   }
/*     */ 
/*     */   public boolean isBucketGeneric() {
/* 165 */     if (this.eventClass == null)
/* 166 */       return true;
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   public Class getBucketClass()
/*     */   {
/* 175 */     return this.eventClass;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.SWVStatisticalTimelineBuckets
 * JD-Core Version:    0.6.2
 */