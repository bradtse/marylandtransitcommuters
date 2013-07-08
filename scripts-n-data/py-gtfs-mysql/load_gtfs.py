#!/bin/env/python

import csv
import MySQLdb
import settings
from subprocess import Popen, PIPE

def is_numeric(s):
    try:
      i = float(s)
    except ValueError:
        # not numeric
        return False
    else:
        # numeric
        return True

def main():
    print "Creating database: {0}".format(settings.MYSQL_DATABASE)
    process = Popen("mysql -u{0} -p{1}".format(settings.MYSQL_USER,
                               settings.MYSQL_PASSWORD), stdout=PIPE,
                               stdin=PIPE, shell=True);
    output = process.communicate("source sql/db_setup.sql")[0];

    conn = MySQLdb.connect (host=settings.MYSQL_HOST, user=settings.MYSQL_USER,
                            passwd=settings.MYSQL_PASSWORD, db=settings.MYSQL_DATABASE)
    cursor = conn.cursor()

    TABLES = ['agency', 'calendar', 'calendar_dates', 'routes', 'shapes', 'stops', 'stop_times', 'trips']

    for table in TABLES:
        print 'processing %s' % table
        f = open('gtfs/%s.txt' % table, 'r')
        reader = csv.reader(f)
        columns = reader.next()
        print columns;
        for row in reader:
            if not row:
                continue
            insert_row = []
            for value in row:
                if value == '':
                    insert_row.append('NULL')
                elif not is_numeric(value):
                    insert_row.append('"' + MySQLdb.escape_string(value) + '"')
                else:
                    insert_row.append(value)

            print insert_row
            insert_sql = "INSERT INTO %s (%s) VALUES (%s);" % (table, ','.join(columns), ','.join(insert_row))
            cursor.execute(insert_sql)

    cursor.close ()
    conn.commit()
    conn.close ()

    print "Converting timestamps"
    Popen("python build_indices.py", shell=True)
    Popen.wait()

    print "Dumping schema"
    fd = open('create_gtfs_db.sql', 'w')
    Popen("mysqldump {0} -u{1} -p{2}".format(settings.MYSQL_DATABASE,
                     settings.MYSQL_USER, settings.MYSQL_PASSWORD),
                     stdout=fd,
                     shell=True)
    fd.close()

if __name__ == '__main__':
    main()
