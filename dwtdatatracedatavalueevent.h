#ifndef DWTDATATRACEDATAVALUEEVENT_H
#define DWTDATATRACEDATAVALUEEVENT_H

#include "itmevent.h"

class DWTDataTraceDataValueEvent : public ITMEvent
{
public:
    DWTDataTraceDataValueEvent(byte comparatorId, byte access, long dataValue);
};

#endif // DWTDATATRACEDATAVALUEEVENT_H
