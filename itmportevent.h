#ifndef ITMPORTEVENT_H
#define ITMPORTEVENT_H

#include "itmevent.h"

class ITMPortEvent : public ITMEvent
{
public:
    ITMPortEvent(quint8 pageNumber, quint8 portNumber, long data, int size);
};

#endif // ITMPORTEVENT_H
