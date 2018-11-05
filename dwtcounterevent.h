#ifndef DWTCOUNTEREVENT_H
#define DWTCOUNTEREVENT_H

#include "itmevent.h"

class DWTCounterEvent : public ITMEvent
{
public:
    DWTCounterEvent(quint8 counters);
private:
    quint8 m_counters;
};

#endif // DWTCOUNTEREVENT_H
