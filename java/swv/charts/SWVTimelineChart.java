/*     */ package com.atollic.truestudio.swv.charts;
/*     */ 
/*     */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*     */ import com.atollic.truestudio.swv.model.SWVStatisticalTimelineBuckets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.eclipse.jface.viewers.TableViewer;
/*     */ import org.eclipse.jface.viewers.TableViewerColumn;
/*     */ import org.eclipse.swt.graphics.Color;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.FillLayout;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Table;
/*     */ import org.eclipse.swt.widgets.TableColumn;
/*     */ import org.eclipse.swt.widgets.Widget;
/*     */ 
/*     */ public class SWVTimelineChart extends SWVChart
/*     */ {
/*     */   private ArrayList<ConcurrentHashMap<String, Integer>> spanEventStatistics;
/*     */   private ArrayList<com.atollic.truestudio.swv.model.Event[]> eventSerie;
/*     */   public Class eventClass;
/*  38 */   private Label toolTipLabel = null;
/*  39 */   private TableViewer toolTipTableViewer = null;
/*  40 */   private Table toolTipTable = null;
/*     */ 
/*     */   public SWVTimelineChart(Composite parent, int scale_type, String title, Color startColor)
/*     */   {
/*  56 */     super(parent, scale_type, title, startColor, true);
/*     */   }
/*     */ 
/*     */   private com.atollic.truestudio.swv.model.Event[] getEvents(double position)
/*     */   {
/*  64 */     com.atollic.truestudio.swv.model.Event[] the_events = (com.atollic.truestudio.swv.model.Event[])this.eventSerie.get(getSerieIndex(position));
/*  65 */     ArrayList eventList = new ArrayList();
/*  66 */     for (com.atollic.truestudio.swv.model.Event event : the_events) {
/*  67 */       if (event != null) {
/*  68 */         eventList.add(event);
/*     */       }
/*     */     }
/*  71 */     the_events = (com.atollic.truestudio.swv.model.Event[])eventList.toArray(new com.atollic.truestudio.swv.model.Event[eventList.size()]);
/*  72 */     return the_events;
/*     */   }
/*     */ 
/*     */   private void setSpanEventStatistics(ArrayList<ConcurrentHashMap<String, Integer>> spanEventStatistics)
/*     */   {
/*  82 */     this.spanEventStatistics = spanEventStatistics;
/*     */   }
/*     */ 
/*     */   private ArrayList<ConcurrentHashMap<String, Integer>> getSpanEventStatistics()
/*     */   {
/*  91 */     return this.spanEventStatistics;
/*     */   }
/*     */ 
/*     */   private String getIndexText(int index)
/*     */   {
/*     */     String toolTip;
/*     */     String toolTip;
/* 104 */     if (getScale() == 0) {
/* 105 */       long startTime = ()getXserie()[index];
/* 106 */       long endTime = index < getXserie().length ? ()getXserie()[(index + 1)] : ()getSeriesLastX();
/* 107 */       toolTip = String.format("%dc - %dc", new Object[] { Long.valueOf(startTime), Long.valueOf(endTime) });
/*     */     } else {
/* 109 */       double startTime = getXserie()[index];
/* 110 */       double endTime = index < getXserie().length ? getXserie()[(index + 1)] : getSeriesLastX();
/* 111 */       toolTip = String.format("%fs - %fs", new Object[] { Double.valueOf(startTime), Double.valueOf(endTime) });
/*     */     }
/* 113 */     return toolTip;
/*     */   }
/*     */ 
/*     */   private String getEventText(double position)
/*     */   {
/* 123 */     int index = getSerieIndex(position);
/* 124 */     ConcurrentHashMap oneSpanEventStatistics = (ConcurrentHashMap)getSpanEventStatistics().get(index);
/* 125 */     Enumeration e = oneSpanEventStatistics.keys();
/* 126 */     List list = Collections.list(e);
/* 127 */     Collections.sort(list);
/* 128 */     String toolTip = getIndexText(index) + "\n" + Messages.SWVTimelineChart_NUMBER_OF_EVENTS + ":\n";
/* 129 */     for (String key : list) {
/* 130 */       toolTip = toolTip + ((Integer)oneSpanEventStatistics.get(key)).toString() + "\t" + key + "\n";
/*     */     }
/* 132 */     return toolTip;
/*     */   }
/*     */ 
/*     */   public void handleToolTipEvent(org.eclipse.swt.widgets.Event event)
/*     */   {
/* 140 */     int xtraMargin = 0;
/* 141 */     double x = event.x;
/* 142 */     double selection = getSelectionFromPixel(x);
/* 143 */     int yHeight = getYValueAt(selection);
/* 144 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/* 145 */       getToolTip().dispose();
/* 146 */     if (yHeight > 0) {
/* 147 */       setToolTip(new Shell(getShell(), 16388));
/* 148 */       if ((!isSerieOfGenericEvents()) && (yHeight <= 40)) {
/* 149 */         xtraMargin = -9;
/* 150 */         getToolTip().setForeground(this.INFO_FOREGROUND_COLOR);
/* 151 */         getToolTip().setBackground(this.INFO_BACKGROUND_COLOR);
/* 152 */         this.toolTipLabel = new Label(getToolTip(), 0);
/* 153 */         this.toolTipLabel.setForeground(this.INFO_FOREGROUND_COLOR);
/* 154 */         this.toolTipLabel.setBackground(this.INFO_BACKGROUND_COLOR);
/* 155 */         this.toolTipLabel.setText(getEventText(selection));
/* 156 */         this.toolTipTableViewer = new TableViewer(getToolTip(), 67584);
/*     */ 
/* 158 */         if (DWTExceptionEvent.class.equals(this.eventClass))
/*     */         {
/* 160 */           Table table = this.toolTipTableViewer.getTable();
/* 161 */           table.addMouseListener(new SWVTimeLineToolTipLogListener(this.toolTipTableViewer));
/*     */         }
/*     */ 
/* 164 */         TableViewerColumn tableViewerColumn = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 165 */         TableColumn tblclmnComparator = tableViewerColumn.getColumn();
/* 166 */         tblclmnComparator.setWidth(50);
/* 167 */         tblclmnComparator.setText(Messages.SWVTimelineChart_INDEX);
/*     */ 
/* 169 */         TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 170 */         TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
/* 171 */         tblclmnValue.setWidth(120);
/* 172 */         tblclmnValue.setText(Messages.SWVTimelineChart_TYPE);
/*     */ 
/* 174 */         TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 175 */         TableColumn tblclmnPc = tableViewerColumn_2.getColumn();
/* 176 */         tblclmnPc.setWidth(120);
/* 177 */         tblclmnPc.setText(Messages.SWVTimelineChart_DATA);
/*     */ 
/* 179 */         TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 180 */         TableColumn tblclmnCycles = tableViewerColumn_3.getColumn();
/* 181 */         tblclmnCycles.setWidth(80);
/* 182 */         tblclmnCycles.setText(Messages.SWVTimelineChart_CYCLES);
/*     */ 
/* 184 */         TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 185 */         TableColumn tblclmnTimes = tableViewerColumn_4.getColumn();
/* 186 */         tblclmnTimes.setWidth(130);
/* 187 */         tblclmnTimes.setText(Messages.SWVTimelineChart_TIMES);
/*     */ 
/* 189 */         TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 190 */         TableColumn tblclmnExtra = tableViewerColumn_5.getColumn();
/* 191 */         tblclmnExtra.setWidth(130);
/* 192 */         tblclmnExtra.setText(Messages.SWVTimelineChart_EXTRA);
/*     */ 
/* 194 */         this.toolTipTableViewer.setLabelProvider(new SWVTimelineChartToolTipLabelProvider());
/* 195 */         this.toolTipTableViewer.setContentProvider(new SWVTimelineChartToolTipContentProvider());
/* 196 */         com.atollic.truestudio.swv.model.Event[] swvEvents = getEvents(selection);
/* 197 */         this.toolTipTableViewer.setInput(swvEvents);
/*     */ 
/* 199 */         getToolTip().setLayout(new GridLayout());
/*     */ 
/* 201 */         this.toolTipTable = this.toolTipTableViewer.getTable();
/* 202 */         this.toolTipTable.setLinesVisible(true);
/* 203 */         this.toolTipTable.setHeaderVisible(true);
/* 204 */         this.toolTipTable.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/* 205 */         this.toolTipTable.setForeground(this.INFO_FOREGROUND_COLOR);
/* 206 */         this.toolTipTable.setBackground(this.INFO_BACKGROUND_COLOR);
/*     */       } else {
/* 208 */         xtraMargin = -3;
/* 209 */         getToolTip().setLayout(new FillLayout());
/* 210 */         this.toolTipLabel = new Label(getToolTip(), 0);
/* 211 */         this.toolTipLabel.setForeground(this.INFO_FOREGROUND_COLOR);
/* 212 */         this.toolTipLabel.setBackground(this.INFO_BACKGROUND_COLOR);
/* 213 */         this.toolTipLabel.setText(getEventText(selection));
/*     */       }
/* 215 */       Point toolTipSize = getToolTip().computeSize(-1, -1);
/* 216 */       Rectangle rect = getPlotArea().getBounds();
/* 217 */       Point pt = toDisplay(rect.x, rect.y);
/* 218 */       int xStart = pt.x + event.x - xtraMargin;
/* 219 */       int xPlotSize = getSize().x;
/* 220 */       if (xStart + toolTipSize.x > xPlotSize + pt.x) {
/* 221 */         xStart = xPlotSize + pt.x - toolTipSize.x;
/* 222 */         if (xStart < 0) xStart = 0;
/*     */       }
/* 224 */       int yStart = pt.y + event.y - toolTipSize.y + xtraMargin;
/* 225 */       getToolTip().setBounds(xStart, yStart, toolTipSize.x + 2, toolTipSize.y + 1);
/* 226 */       getToolTip().setVisible(true);
/* 227 */       this.toolTipLabel.addListener(7, new Listener()
/*     */       {
/*     */         public void handleEvent(org.eclipse.swt.widgets.Event inner_event)
/*     */         {
/* 231 */           Shell tip = ((Control)inner_event.widget).getShell();
/* 232 */           switch (inner_event.type) {
/*     */           case 7:
/* 234 */             tip.dispose();
/*     */           }
/*     */         }
/*     */       });
/* 239 */       getToolTip().addListener(3, new Listener()
/*     */       {
/*     */         public void handleEvent(org.eclipse.swt.widgets.Event inner_event)
/*     */         {
/* 243 */           inner_event.widget.dispose();
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSeriesFromBuckets(SWVStatisticalTimelineBuckets buckets)
/*     */   {
/* 256 */     if (buckets != null) {
/* 257 */       double[] new_Xserie = buckets.getXarray(getScale());
/*     */ 
/* 259 */       setSeriesFirstX(buckets.getXMin(getScale()));
/* 260 */       setSeriesLastX(buckets.getXMax(getScale()));
/*     */ 
/* 262 */       if (new_Xserie.length > 1)
/*     */       {
/* 264 */         double[] new_Yserie = buckets.getYarray();
/* 265 */         if (!hasPerm()) {
/* 266 */           Arrays.fill(new_Yserie, 0.0D);
/*     */         }
/* 268 */         double buckets_yMax = buckets.getYMax();
/* 269 */         if ((buckets_yMax > 0.0D) && (buckets_yMax > getRealYMax()))
/*     */         {
/* 271 */           setYMax(buckets_yMax);
/*     */         }
/*     */ 
/* 274 */         setSerieOfGenericEvents(buckets.isBucketGeneric());
/* 275 */         setSpanEventStatistics(buckets.getSpanEventStatisticsSet());
/* 276 */         if (!isSerieOfGenericEvents()) {
/* 277 */           this.eventSerie = buckets.getToolTipEvents();
/*     */         }
/*     */ 
/* 280 */         setSeries(new_Xserie, new_Yserie);
/*     */       }
/* 282 */       setCoreClock(buckets.getSWVCoreClock());
/* 283 */       this.eventClass = buckets.getBucketClass();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.charts.SWVTimelineChart
 * JD-Core Version:    0.6.2
 */