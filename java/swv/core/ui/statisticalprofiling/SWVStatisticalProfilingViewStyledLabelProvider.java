/*    */ package com.atollic.truestudio.swv.core.ui.statisticalprofiling;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import org.eclipse.jface.viewers.StyledCellLabelProvider;
/*    */ import org.eclipse.swt.graphics.Color;
/*    */ import org.eclipse.swt.graphics.FontMetrics;
/*    */ import org.eclipse.swt.graphics.GC;
/*    */ import org.eclipse.swt.graphics.Rectangle;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ import org.eclipse.swt.widgets.Event;
/*    */ import org.eclipse.swt.widgets.Table;
/*    */ import org.eclipse.swt.widgets.TableColumn;
/*    */ import org.eclipse.swt.widgets.Widget;
/*    */ 
/*    */ class SWVStatisticalProfilingViewStyledLabelProvider extends StyledCellLabelProvider
/*    */   implements SWVStatisticalProfilingViewLabelProviderInterface
/*    */ {
/* 20 */   private final Color colorbg = Display.getDefault().getSystemColor(32);
/*    */ 
/*    */   protected void paint(Event event, Object element)
/*    */   {
/* 24 */     Widget widget = event.widget;
/* 25 */     super.paint(event, element);
/* 26 */     String inuse = null;
/* 27 */     float f = -1.0F;
/* 28 */     if ((element instanceof SWVStatisticalProfilingItem)) {
/* 29 */       f = ((SWVStatisticalProfilingItem)element).getPercentInUse();
/* 30 */       DecimalFormat dFormat = new DecimalFormat("#.##");
/* 31 */       dFormat.setMinimumFractionDigits(2);
/* 32 */       inuse = dFormat.format(f) + "%";
/*    */ 
/* 34 */       if (f != -1.0F) {
/* 35 */         GC gc = event.gc;
/* 36 */         int width = 0;
/*    */ 
/* 38 */         if ((widget != null) && ((widget instanceof Table))) {
/* 39 */           width = (int)(((Table)widget).getColumn(1).getWidth() * f / 100.0F + 0.5F);
/*    */         } else {
/* 41 */           Rectangle rect = gc.getClipping();
/* 42 */           width = (int)(rect.width * f / 100.0F + 0.5F);
/*    */         }
/* 44 */         int x = event.x;
/* 45 */         int y = event.y;
/* 46 */         int height = event.height;
/*    */ 
/* 48 */         Color oldBackground = gc.getBackground();
/* 49 */         gc.setBackground(this.colorbg);
/* 50 */         gc.fillRectangle(x, y, width, height);
/* 51 */         gc.setBackground(oldBackground);
/*    */ 
/* 54 */         int hdiff = gc.getFontMetrics().getHeight();
/* 55 */         int wdiff = gc.getFontMetrics().getAverageCharWidth();
/* 56 */         hdiff = (int)((height - hdiff) / 2.0F + 0.5F);
/* 57 */         if (hdiff > 0)
/* 58 */           y += hdiff;
/* 59 */         gc.drawText(inuse, x + wdiff, y, true);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.statisticalprofiling.SWVStatisticalProfilingViewStyledLabelProvider
 * JD-Core Version:    0.6.2
 */