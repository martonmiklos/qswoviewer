#ifndef DWTCOUNTEREVENT_H
#define DWTCOUNTEREVENT_H

#include "itmevent.h"

class DWTCounterEvent : public ITMEvent
{
public:
    DWTCounterEvent(byte counters);
};

#endif // DWTCOUNTEREVENT_H
