/*     */ package com.atollic.truestudio.swv.charts;
/*     */ 
/*     */ import com.atollic.truestudio.swv.core.ui.datatrace.Data;
/*     */ import com.atollic.truestudio.swv.core.ui.datatrace.DataTrace;
/*     */ import com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.eclipse.swt.graphics.Color;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.swtchart.IAxis;
/*     */ import org.swtchart.IAxisSet;
/*     */ import org.swtchart.IAxisTick;
/*     */ 
/*     */ public class SWVDataTraceTimelineChart extends SWVChart
/*     */ {
/*     */   private String oldCacheHash;
/*     */ 
/*     */   /** @deprecated */
/*     */   public SWVDataTraceTimelineChart(Composite parent, int scale_type, String title, Color startColor, boolean hasXAxis, double coreClock)
/*     */   {
/*  35 */     super(parent, scale_type, title, startColor, false);
/*  36 */     if (!hasXAxis) {
/*  37 */       IAxisTick xTick = getAxisSet().getXAxis(0).getTick();
/*  38 */       xTick.setVisible(false);
/*     */     }
/*  40 */     setSerieOfGenericEvents(false);
/*  41 */     enablePlotSymbol(true);
/*  42 */     setCoreClock(coreClock);
/*  43 */     setMinDisplayedYMax(0.0D);
/*  44 */     setScale(0);
/*     */   }
/*     */ 
/*     */   public void setSeriesFromDataTrace(DataTrace dataTrace)
/*     */   {
/*  81 */     setSeriesFromDataTrace(dataTrace, -1L);
/*     */   }
/*     */ 
/*     */   public void setSeriesFromDataTrace(DataTrace dataTrace, long endCycle)
/*     */   {
/*  92 */     if (isSizeToBeChanged()) {
/*  93 */       setPixelWidth();
/*  94 */       setSpan(getXMin(), getXMin() + getOptimalWidth());
/*  95 */       setSizeIsToBeChanged(false);
/*     */     }
/*     */ 
/* 110 */     if ((dataTrace == null) || (dataTrace.getLastData() == null)) {
/* 111 */       return;
/*     */     }
/* 113 */     if (isSizeToBeChanged()) {
/* 114 */       setPixelWidth();
/* 115 */       setSpan(getXMin(), getXMin() + getOptimalWidth());
/* 116 */       setSizeIsToBeChanged(false);
/*     */     }
/* 118 */     if (endCycle < 0L) {
/* 119 */       endCycle = dataTrace.getLastData().getCycles();
/*     */     }
/* 121 */     long current_run_size = ()(getCoreClock() * getOptimalWidthInSeconds());
/* 122 */     long startCycle = endCycle - current_run_size < 0L ? 0L : endCycle - current_run_size;
/*     */ 
/* 124 */     if (getScale() == 1) {
/* 125 */       setSeriesLastX(endCycle / getCoreClock());
/* 126 */       setSeriesFirstX(startCycle / getCoreClock());
/*     */     } else {
/* 128 */       setSeriesLastX(endCycle);
/* 129 */       setSeriesFirstX(startCycle);
/*     */     }
/*     */ 
/* 132 */     String newCacheHash = String.format("%d%d%s", new Object[] { Long.valueOf(endCycle), Byte.valueOf(dataTrace.getComparatorId()), dataTrace.getName() });
/* 133 */     if (newCacheHash.equals(this.oldCacheHash)) {
/* 134 */       return;
/*     */     }
/* 136 */     this.oldCacheHash = newCacheHash;
/*     */ 
/* 138 */     ArrayList vals = dataTrace.getHistory();
/* 139 */     if (vals == null) {
/* 140 */       return;
/*     */     }
/* 142 */     Iterator iter = vals.iterator();
/* 143 */     double[] y = new double[vals.size()];
/* 144 */     double[] x = new double[vals.size()];
/*     */ 
/* 146 */     int i = 0;
/* 147 */     double latestX = 0.0D;
/* 148 */     double currentX = 0.0D;
/* 149 */     double yMinOfSelection = 0.0D;
/* 150 */     double yMaxOfSelection = 0.0D;
/* 151 */     int nrOfValuesInGraph = 0;
/* 152 */     while (iter.hasNext()) {
/* 153 */       Data data = (Data)iter.next();
/* 154 */       currentX = getScale() == 1 ? data.getCycles() / getCoreClock() : data.getCycles();
/* 155 */       if (currentX >= latestX) {
/* 156 */         x[i] = currentX;
/* 157 */         latestX = currentX;
/* 158 */         y[i] = data.getDataValue();
/*     */ 
/* 160 */         if ((data.getCycles() >= startCycle) && (data.getCycles() <= endCycle)) {
/* 161 */           if (y[i] < yMinOfSelection) {
/* 162 */             yMinOfSelection = y[i];
/*     */           }
/* 164 */           if (y[i] > yMaxOfSelection) {
/* 165 */             yMaxOfSelection = y[i];
/*     */           }
/* 167 */           nrOfValuesInGraph++;
/*     */         }
/*     */       } else {
/* 170 */         x[i] = currentX;
/* 171 */         y[i] = data.getDataValue();
/*     */       }
/* 173 */       i++;
/*     */     }
/* 175 */     if (yMaxOfSelection > 0.0D) {
/* 176 */       setYMax(yMaxOfSelection);
/*     */     }
/* 178 */     if (yMinOfSelection < 0.0D) {
/* 179 */       setYMin(yMinOfSelection);
/*     */     }
/*     */ 
/* 182 */     checkPlotSymbol(nrOfValuesInGraph);
/*     */ 
/* 184 */     setSeries(x, y);
/* 185 */     showAll();
/* 186 */     update();
/*     */   }
/*     */ 
/*     */   private void checkPlotSymbol(int nrOfValuesInGraph)
/*     */   {
/* 201 */     if (nrOfValuesInGraph > getOptimalNrBars())
/* 202 */       enablePlotSymbol(false);
/*     */     else
/* 204 */       enablePlotSymbol(true);
/*     */   }
/*     */ 
/*     */   public void setSeriesFromDataTrace2(DataTrace dataTrace)
/*     */   {
/* 210 */     if (dataTrace != null) {
/* 211 */       ArrayList valueHistory = dataTrace.getHistory();
/* 212 */       if (valueHistory != null) {
/* 213 */         double prevXLast = getSeriesLastX();
/*     */ 
/* 216 */         if (valueHistory.size() > getXserie().length)
/*     */         {
/* 218 */           Iterator iter = valueHistory.iterator();
/* 219 */           double[] y = new double[valueHistory.size()];
/* 220 */           double[] x = new double[valueHistory.size()];
/*     */ 
/* 222 */           int i = 0;
/* 223 */           while (iter.hasNext()) {
/* 224 */             Data data = (Data)iter.next();
/*     */ 
/* 226 */             y[i] = Double.parseDouble(SWVDataTraceUtil.convert(data.getDataValue(), data.getType(), 5));
/* 227 */             x[i] = (data.getCycles() / 16000000.0D);
/* 228 */             i++;
/*     */           }
/*     */ 
/* 234 */           double xLast = x[(x.length - 1)];
/* 235 */           setSeriesLastX(xLast);
/*     */ 
/* 239 */           double diff = xLast - prevXLast;
/* 240 */           double prevXFirst = getFirstX();
/* 241 */           setSeriesFirstX(prevXFirst + diff);
/*     */ 
/* 243 */           setSeries(x, y);
/* 244 */           getAxisSet().adjustRange();
/* 245 */           showAll();
/* 246 */           update();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleToolTipEvent(Event event)
/*     */   {
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.charts.SWVDataTraceTimelineChart
 * JD-Core Version:    0.6.2
 */