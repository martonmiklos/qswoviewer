#ifndef MESSAGES_H
#define MESSAGES_H

#include <QString>

class Messages
{
public:
    Messages();
    // FIXME load
    static QString DWTCounterEvent_COUNTER_OVERFLOW;
    static QString DWTCounterEvent_CPI;
    static QString DWTCounterEvent_CYC;
    static QString DWTCounterEvent_EXC;
    static QString DWTCounterEvent_FOLD;
    static QString DWTCounterEvent_LSU;
    static QString DWTCounterEvent_SLEEP;
    static QString DWTDataTraceAddressOffsetEvent_DATA_TRACE_ADDRESS_OFFSET;
    static QString DWTDataTraceDataValueEvent_0;
    static QString DWTDataTraceDataValueEvent_R;
    static QString DWTDataTraceDataValueEvent_W;
    static QString DWTDataTracePCValueEvent_DATA_PC_VALUE;
    static QString DWTExceptionEvent_BUS_FAULT;
    static QString DWTExceptionEvent_DEBUG_MONITOR;
    static QString DWTExceptionEvent_ENTRY;
    static QString DWTExceptionEvent_EXCEPTION;
    static QString DWTExceptionEvent_EXIT;
    static QString DWTExceptionEvent_HARD_FAULT;
    static QString DWTExceptionEvent_MEMORY_MANAGE_FAULT;
    static QString DWTExceptionEvent_NMI;
    static QString DWTExceptionEvent_PENDSV;
    static QString DWTExceptionEvent_RESERVED;
    static QString DWTExceptionEvent_RESET;
    static QString DWTExceptionEvent_RETURN;
    static QString DWTExceptionEvent_SVCALL;
    static QString DWTExceptionEvent_SYSTICK;
    static QString DWTExceptionEvent_USAGE_FAULT;
    static QString DWTPCSampleEvent_PC_SAMPLE;
    static QString Event_NO_TIMESTAMP_RECEIVED_CYCLE_VALUE_GUESSED;
    static QString Event_PACKET_DELAYED;
    static QString Event_TIMESTAMP_DELAYED;
    static QString InterruptInfo_EXC;
    static QString InterruptInfo_IRQ;
    static QString InterruptInfo_TOTAL_FOR_ALL;
    static QString ITMExtensionEvent_EXTENSION;
    static QString ITMGlobalTimestampEvent_GLOBAL_TIMESTAMP_BITS;
    static QString ITMOverflowEvent_GENERATED_DUE_TO_WAITING_FOR_SYNC;
    static QString ITMOverflowEvent_OVERFLOW;
    static QString ITMPortEvent_ITM_PORT;
    static QString ITMPortEvent_PAGE;
    static QString ITMSyncEvent_SYNC;
    static QString MODEL_COMPARATOR;
    static QString MODEL_NA;
    static QString MODEL_UNKNOWN;
};

#endif // MESSAGES_H
