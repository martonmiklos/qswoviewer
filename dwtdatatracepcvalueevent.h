#ifndef DWTDATATRACEPCVALUEEVENT_H
#define DWTDATATRACEPCVALUEEVENT_H


#include "itmevent.h"

class DWTDataTracePCValueEvent : public ITMEvent
{
public:
    DWTDataTracePCValueEvent(byte comparatorId, long pcAddress);
};

#endif // DWTDATATRACEPCVALUEEVENT_H
