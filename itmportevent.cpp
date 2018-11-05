#include "itmportevent.h"

ITMPortEvent::ITMPortEvent(quint8 pageNumber, quint8 portNumber, long data, int size) :
    ITMEvent(),
    m_pageNumber(pageNumber),
    m_portNumber(portNumber),
    m_data(data),
    m_size(size)
{

}
