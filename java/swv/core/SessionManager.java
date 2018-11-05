/*     */ package com.atollic.truestudio.swv.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.eclipse.cdt.dsf.datamodel.IDMContext;
/*     */ import org.eclipse.cdt.dsf.datamodel.IDMEvent;
/*     */ import org.eclipse.cdt.dsf.debug.model.DsfLaunch;
/*     */ import org.eclipse.cdt.dsf.debug.service.IRunControl.IExecutionDMContext;
/*     */ import org.eclipse.cdt.dsf.debug.service.IRunControl.IExitedDMEvent;
/*     */ import org.eclipse.cdt.dsf.debug.service.IRunControl.IResumedDMEvent;
/*     */ import org.eclipse.cdt.dsf.debug.service.IRunControl.ISuspendedDMEvent;
/*     */ import org.eclipse.cdt.dsf.debug.service.command.ICommandControlService.ICommandControlShutdownDMEvent;
/*     */ import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
/*     */ import org.eclipse.cdt.dsf.gdb.service.command.GDBControlDMContext;
/*     */ import org.eclipse.cdt.dsf.service.DsfServiceEventHandler;
/*     */ import org.eclipse.cdt.dsf.service.DsfSession;
/*     */ import org.eclipse.core.commands.Command;
/*     */ import org.eclipse.core.commands.ExecutionException;
/*     */ import org.eclipse.core.commands.State;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.ILog;
/*     */ import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.debug.core.ILaunch;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.debug.internal.ui.DebugUIPlugin;
/*     */ import org.eclipse.debug.ui.DebugUITools;
/*     */ import org.eclipse.debug.ui.contexts.DebugContextEvent;
/*     */ import org.eclipse.debug.ui.contexts.IDebugContextListener;
/*     */ import org.eclipse.debug.ui.contexts.IDebugContextManager;
/*     */ import org.eclipse.jface.viewers.StructuredSelection;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.commands.ICommandService;
/*     */ import org.eclipse.ui.handlers.HandlerUtil;
/*     */ 
/*     */ public class SessionManager
/*     */   implements IDebugContextListener
/*     */ {
/*     */   private final Map<DsfSession, SWVClient> fClients;
/*  52 */   private ILaunch fActiveLaunch = null;
/*  53 */   private DsfSession fActiveSession = null;
/*  54 */   private boolean fIsTracing = false;
/*  55 */   private boolean fCouldTrace = false;
/*  56 */   private boolean fSuspended = true;
/*     */   private List<ISWVEventListener> fViewListeners;
/*     */ 
/*     */   public SessionManager()
/*     */   {
/*  61 */     this.fClients = new ConcurrentHashMap();
/*  62 */     this.fViewListeners = new ArrayList();
/*     */ 
/*  65 */     DebugUITools.getDebugContextManager().addDebugContextListener(this);
/*     */   }
/*     */ 
/*     */   public boolean createSession(ILaunchConfiguration config, DsfSession session, String host, String port, int swoClock, int swvCoreClock, int traceDiv)
/*     */   {
/*  78 */     if ((session == null) || (host == null) || (host.isEmpty())) {
/*  79 */       SWVPlugin.getDefault().getLog().log(new Status(4, "TrueSTUDIO", "SWV SessionManager, createSession(), Bad parameters"));
/*  80 */       return false;
/*     */     }
/*     */ 
/*  84 */     SWVClient swvClient = new SWVClient(session, config);
/*     */ 
/*  87 */     this.fClients.put(session, swvClient);
/*     */     try
/*     */     {
/*  91 */       swvClient.connect(host, Integer.parseInt(port), swoClock, swvCoreClock, traceDiv);
/*     */     }
/*     */     catch (Exception e) {
/*  94 */       this.fClients.remove(session);
/*     */ 
/*  96 */       SWVPlugin.log(new Status(4, "com.atollic.truestudio.swv.core", "SWV Session manager - Create session failed"));
/*  97 */       return false;
/*     */     }
/*     */ 
/* 101 */     if ((this.fActiveSession != null) && (session.equals(this.fActiveSession))) {
/* 102 */       swvClient.setSuspended(this.fSuspended);
/* 103 */       notifyListeners(2);
/*     */     }
/*     */ 
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   public ILaunchConfigurationWorkingCopy getActiveConfiguration()
/*     */   {
/* 114 */     if (this.fActiveLaunch != null) {
/*     */       try {
/* 116 */         return this.fActiveLaunch.getLaunchConfiguration().getWorkingCopy();
/*     */       } catch (CoreException e) {
/* 118 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized SWVClient getClient()
/*     */   {
/* 129 */     if (this.fActiveSession == null) {
/* 130 */       return null;
/*     */     }
/*     */ 
/* 133 */     return (SWVClient)this.fClients.get(this.fActiveSession);
/*     */   }
/*     */ 
/*     */   public synchronized SWVClient getClient(DsfSession session) {
/* 137 */     if (session == null) {
/* 138 */       return null;
/*     */     }
/*     */ 
/* 141 */     return (SWVClient)this.fClients.get(session);
/*     */   }
/*     */ 
/*     */   private void notifyListeners(final int event) {
/* 145 */     Display.getDefault().asyncExec(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         List listeners;
/* 150 */         synchronized (SessionManager.this.fViewListeners) {
/* 151 */           listeners = new ArrayList(SessionManager.this.fViewListeners);
/*     */         }
/*     */         List listeners;
/* 154 */         for (ISWVEventListener listener : listeners)
/* 155 */           listener.handleSWVEvent(event);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void addSWVEventListener(ISWVEventListener listener)
/*     */   {
/* 166 */     synchronized (this.fViewListeners) {
/* 167 */       if (!this.fViewListeners.contains(listener))
/* 168 */         this.fViewListeners.add(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeSWVEventListener(ISWVEventListener listener)
/*     */   {
/* 178 */     synchronized (this.fViewListeners) {
/* 179 */       this.fViewListeners.remove(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void setActiveSession(DsfSession session)
/*     */   {
/* 185 */     if (this.fActiveSession != null) {
/* 186 */       this.fActiveSession.removeServiceEventListener(this);
/*     */     }
/*     */ 
/* 189 */     this.fActiveSession = session;
/*     */ 
/* 191 */     if (this.fActiveSession == null) {
/* 192 */       return;
/*     */     }
/*     */ 
/* 195 */     SWVClient client = (SWVClient)this.fClients.get(this.fActiveSession);
/*     */ 
/* 198 */     notifyListeners(client == null ? 3 : 2);
/*     */ 
/* 201 */     ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
/* 202 */     Command command = commandService.getCommand("com.atollic.truestudio.swv.core.start_trace");
/* 203 */     boolean commandState = ((Boolean)command.getState("org.eclipse.ui.commands.toggleState").getValue()).booleanValue();
/*     */ 
/* 205 */     if (client != null)
/*     */     {
/* 207 */       this.fCouldTrace = true;
/*     */ 
/* 209 */       if (client.isTracing())
/*     */       {
/* 211 */         this.fIsTracing = true;
/*     */ 
/* 213 */         if (!commandState)
/*     */           try {
/* 215 */             HandlerUtil.toggleCommandState(command);
/*     */           }
/*     */           catch (ExecutionException e) {
/* 218 */             e.printStackTrace();
/*     */           }
/*     */       }
/*     */       else
/*     */       {
/* 223 */         this.fIsTracing = false;
/*     */ 
/* 225 */         if (commandState)
/*     */           try {
/* 227 */             HandlerUtil.toggleCommandState(command);
/*     */           }
/*     */           catch (ExecutionException e) {
/* 230 */             e.printStackTrace();
/*     */           }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 236 */       if (commandState) {
/*     */         try {
/* 238 */           HandlerUtil.toggleCommandState(command);
/*     */         }
/*     */         catch (ExecutionException e) {
/* 241 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */ 
/* 245 */       this.fIsTracing = false;
/* 246 */       this.fCouldTrace = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isTracing()
/*     */   {
/* 255 */     return this.fIsTracing;
/*     */   }
/*     */ 
/*     */   public boolean couldTrace()
/*     */   {
/* 263 */     return (this.fCouldTrace) && (this.fSuspended);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 271 */     SWVClient client = getClient();
/* 272 */     if (client != null) {
/* 273 */       client.clear();
/*     */     }
/* 275 */     notifyListeners(4);
/*     */   }
/*     */ 
/*     */   public void debugContextChanged(DebugContextEvent event)
/*     */   {
/* 283 */     Object context = ((StructuredSelection)event.getContext()).getFirstElement();
/* 284 */     ILaunch contextLaunch = DebugUIPlugin.getLaunch(context);
/*     */ 
/* 286 */     if ((contextLaunch instanceof DsfLaunch))
/*     */     {
/* 289 */       this.fActiveLaunch = contextLaunch;
/* 290 */       GdbLaunch gdbLaunch = (GdbLaunch)contextLaunch;
/* 291 */       DsfSession contextSession = gdbLaunch.getSession();
/* 292 */       String contextSessionId = gdbLaunch.getSession().getId();
/*     */ 
/* 295 */       if ((this.fActiveSession == null) || (!this.fActiveSession.equals(contextSessionId))) {
/* 296 */         setActiveSession(contextSession);
/* 297 */         contextSession.addServiceEventListener(this, null);
/*     */       }
/*     */     } else {
/* 300 */       this.fActiveLaunch = null;
/* 301 */       setActiveSession(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   @DsfServiceEventHandler
/*     */   public void handleDsfEvent(Object event)
/*     */   {
/* 313 */     if ((event instanceof IDMEvent)) {
/* 314 */       IDMEvent idmEvent = (IDMEvent)event;
/* 315 */       IDMContext context = idmEvent.getDMContext();
/*     */ 
/* 317 */       if ((context instanceof IRunControl.IExecutionDMContext))
/*     */       {
/* 324 */         if (((idmEvent instanceof IRunControl.ISuspendedDMEvent)) || ((idmEvent instanceof IRunControl.IResumedDMEvent)))
/* 325 */           synchronized (this)
/*     */           {
/* 327 */             this.fSuspended = (idmEvent instanceof IRunControl.ISuspendedDMEvent);
/*     */ 
/* 329 */             SWVClient client = (SWVClient)this.fClients.get(this.fActiveSession);
/* 330 */             if (client != null)
/*     */             {
/* 332 */               client.setSuspended(this.fSuspended);
/*     */ 
/* 336 */               notifyListeners(this.fSuspended ? 0 : 1);
/*     */             }
/*     */           }
/* 339 */         (idmEvent instanceof IRunControl.IExitedDMEvent);
/*     */       }
/* 342 */       else if (((context instanceof GDBControlDMContext)) && 
/* 343 */         ((idmEvent instanceof ICommandControlService.ICommandControlShutdownDMEvent)))
/*     */       {
/* 345 */         SWVClient client = (SWVClient)this.fClients.remove(this.fActiveSession);
/*     */ 
/* 348 */         if (client != null)
/* 349 */           client.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.SessionManager
 * JD-Core Version:    0.6.2
 */