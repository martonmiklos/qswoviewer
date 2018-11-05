/*    */ package com.atollic.truestudio.swv.core.ui.exception;
/*    */ 
/*    */ import com.atollic.truestudio.swv.model.InterruptInfo;
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
/*    */ class SWVExceptionStatisticViewStyledLabelProviderForNumberOf extends StyledCellLabelProvider
/*    */   implements SWVExceptionStatisticsViewLabelProviderInterface
/*    */ {
/* 22 */   private final Color colorbg = Display.getDefault().getSystemColor(32);
/*    */ 
/*    */   protected void paint(Event event, Object element)
/*    */   {
/* 26 */     Widget widget = event.widget;
/* 27 */     super.paint(event, element);
/* 28 */     String inuse = null;
/* 29 */     float f = -1.0F;
/* 30 */     if (((element instanceof InterruptInfo)) && (((InterruptInfo)element).getId() != 0)) {
/* 31 */       InterruptInfo interruptInfo = (InterruptInfo)element;
/* 32 */       if ((!interruptInfo.isTotalInfoObject()) && (interruptInfo.isAccessed()) && (!interruptInfo.getName().isEmpty())) {
/* 33 */         f = interruptInfo.getPercentNumberOf();
/* 34 */         DecimalFormat dFormat = new DecimalFormat("#.####");
/* 35 */         dFormat.setMinimumFractionDigits(4);
/* 36 */         inuse = dFormat.format(f) + "%";
/*    */ 
/* 38 */         if (f != -1.0F) {
/* 39 */           GC gc = event.gc;
/* 40 */           int width = 0;
/*    */ 
/* 42 */           if ((widget != null) && ((widget instanceof Table))) {
/* 43 */             width = (int)(((Table)widget).getColumn(2).getWidth() * f / 100.0F + 0.5F);
/*    */           } else {
/* 45 */             Rectangle rect = gc.getClipping();
/* 46 */             width = (int)(rect.width * f / 100.0F + 0.5F);
/*    */           }
/* 48 */           int x = event.x;
/* 49 */           int y = event.y;
/* 50 */           int height = event.height;
/*    */ 
/* 52 */           Color oldBackground = gc.getBackground();
/* 53 */           gc.setBackground(this.colorbg);
/* 54 */           gc.fillRectangle(x, y, width, height);
/* 55 */           gc.setBackground(oldBackground);
/*    */ 
/* 58 */           int hdiff = gc.getFontMetrics().getHeight();
/* 59 */           int wdiff = gc.getFontMetrics().getAverageCharWidth();
/* 60 */           hdiff = (int)((height - hdiff) / 2.0F + 0.5F);
/* 61 */           if (hdiff > 0) {
/* 62 */             y += hdiff;
/*    */           }
/* 64 */           gc.drawText(inuse, x + wdiff, y, true);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionStatisticViewStyledLabelProviderForNumberOf
 * JD-Core Version:    0.6.2
 */