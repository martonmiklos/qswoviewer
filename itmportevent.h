#ifndef ITMPORTEVENT_H
#define ITMPORTEVENT_H

#include "itmevent.h"

class ITMPortEvent : public ITMEvent
{
public:
    ITMPortEvent(quint8 pageNumber, quint8 portNumber, long data, int size);

private:
    quint8 m_pageNumber;
    quint8 m_portNumber;
    long m_data;
    int m_size;
};

#endif // ITMPORTEVENT_H
