#!/bin/bash
source ~/.netinf/settings
/usr/bin/mysql -u root -p${DB_ROOTPW} < ${DP_LOC}/sql/event_service.sql &> /dev/null
/usr/bin/mysql -u root -p${DB_ROOTPW} < ${DP_LOC}/sql/netinf_node_data.sql &> /dev/null
