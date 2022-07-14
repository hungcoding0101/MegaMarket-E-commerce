import React, { useState } from 'react'
import { useCurrentUser } from '../Hooks/UserHooks';
import { Skeleton, Result, Button, Table, Modal } from "antd";
import { useUpdateNotification } from '../Hooks/NotificationHooks';

 const Notifications = () => {
    const user = useCurrentUser(null, null, true);

    const updateNotifications = useUpdateNotification();

    const [selectedItems, setSelectedItems] = useState([]);

    const handleUpdate = () => {
      updateNotifications.mutate(selectedItems.map(noti => noti.id), {
        onError: (error) => {
          Modal.error({ title: error.response.data.message });
        },
        onSuccess: () => {
          Modal.success({
            title: "Updated",
          });
        },
      });
    }

    const columns = [
      {
        title: "Date",
        dataIndex: "dateSent",
        width: 145,
      },

      {
        title: "Contents",
        render: (text, record, index) => (
          <>
            <div
              style={{
                fontWeight: "bold",
                color: record.status === "NEW" ? "blue" : "black",
              }}
            >
              {record.subject}
            </div>
            <div>{record.contents}</div>
          </>
        ),
      },
    ];

     if (user.isError) {
       return (
         <Result
           status={"error"}
           title="Some errors occurred. Please try again later"
         ></Result>
       );
     }

    return (
      <Skeleton
        active={true}
        loading={user.isLoading || user.data?.data == null}
      >
        <div style={{ marginBottom: 10 }}>
          <Button
            type="primary"
            onClick={handleUpdate}
            disabled={selectedItems.length === 0}
          >
            Mark as read
          </Button>
        </div>
        <Table
          rowKey={(record) => record.id}
          bordered
          dataSource={user.data?.data.notifications}
          columns={columns}
          rowClassName= {(record, index) => {
            if(record.status === 'READ' ){
              return "readNotificationRow";
            }

            return "newNotificationRow";
          }}
          rowSelection={{
            type: "checkbox",
            renderCell: (checked, record, index, originNode) => {
              if (record.status === "NEW") {
                return originNode;
              }
            },
            onChange: (selectedRowKeys, selectedRows) => {
              setSelectedItems(selectedRows);
              console.log(JSON.stringify(selectedRows));
            },
          }}
          scroll={{x: 600,  y: 400 }}
        />
      </Skeleton>
    );
}

export default Notifications;