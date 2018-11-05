#ifndef DWTDATATRACEDATAVALUEEVENT_H
#define DWTDATATRACEDATAVALUEEVENT_H

#include "itmevent.h"

class DWTDataTraceDataValueEvent : public ITMEvent
{
public:
    DWTDataTraceDataValueEvent(quint8 comparatorId, quint8 access, long dataValue);
};

#endif // DWTDATATRACEDATAVALUEEVENT_H
