#include "mainwindow.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QCoreApplication::setOrganizationName("MM");
    QCoreApplication::setApplicationName("QSWOViewer");

    QApplication a(argc, argv);
    MainWindow w;
    w.show();

    return a.exec();
}
