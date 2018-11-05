#ifndef DWTDATATRACEADDRESSOFFSETEVENT_H
#define DWTDATATRACEADDRESSOFFSETEVENT_H

#include "itmevent.h"

class DWTDataTraceAddressOffsetEvent : public ITMEvent
{
public:
    DWTDataTraceAddressOffsetEvent(byte comparatorId, int addressOffset);
};

#endif // DWTDATATRACEADDRESSOFFSETEVENT_H
