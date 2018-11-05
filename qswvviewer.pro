#-------------------------------------------------
#
# Project created by QtCreator 2018-11-04T21:30:55
#
#-------------------------------------------------

QT       += core gui network

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = qswoviewer
TEMPLATE = app

# The following define makes your compiler emit warnings if you use
# any feature of Qt which has been marked as deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if you use deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0


SOURCES += \
        main.cpp \
        mainwindow.cpp \
    swvclient.cpp \
    swvinterruptparser.cpp \
    itmoverflowevent.cpp \
    itmevent.cpp \
    itmlocaltimestampevent.cpp \
    dwtcounterevent.cpp \
    itmglobaltimestampevent.cpp \
    dwtdatatraceaddressoffsetevent.cpp \
    dwtdatatracepcvalueevent.cpp \
    itmextensionevent.cpp \
    itmportevent.cpp \
    dwtdatatracedatavalueevent.cpp \
    dwtpcsampleevent.cpp \
    itmsyncevent.cpp \
    dwtexceptionevent.cpp \
    timestampevent.cpp \
    messages.cpp

HEADERS += \
        mainwindow.h \
    swvclient.h \
    swvinterruptparser.h \
    itmoverflowevent.h \
    itmevent.h \
    itmlocaltimestampevent.h \
    messages.h \
    dwtcounterevent.h \
    itmglobaltimestampevent.h \
    dwtdatatraceaddressoffsetevent.h \
    dwtdatatracepcvalueevent.h \
    itmextensionevent.h \
    itmportevent.h \
    dwtdatatracedatavalueevent.h \
    dwtpcsampleevent.h \
    itmsyncevent.h \
    dwtexceptionevent.h \
    timestampevent.h

FORMS += \
        mainwindow.ui
