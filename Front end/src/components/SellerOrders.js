import { Avatar, Button, Form, List, Modal, Result, Select, Space, Spin, Table } from 'antd';
import { Option } from 'antd/lib/mentions';
import React, { useContext, useEffect, useRef, useState } from 'react'
import { useQueryClient } from 'react-query';
import { Link } from 'react-router-dom';
import { useOwnedProducts, useUpdateOrder } from '../Hooks/SellerHooks';
import { useCurrentUser } from '../Hooks/UserHooks';
import { user_keys } from '../QueryKeys/User_keys';
import { Formatter } from '../Util/Formatter';
import { productUtil } from '../Util/ProductUtil';

export default function SellerOrders() {
  const user = useCurrentUser(null, null, true);
  const queryClient = useQueryClient();

  const [dataSource, setDataSource] = useState(
    user.isSuccess ? user.data.data.orders : []
  );

  const productList = useOwnedProducts(null, null, true);

  const [updatedItems, setUpdatedItems] = useState([]);

  const EditableContext = React.createContext(null);
  const scrollPosition = useRef(0);

  useEffect(() => window.scrollTo(0, scrollPosition), [scrollPosition]);

  const updateStatus = useUpdateOrder();

  const handleUpdate = (values) => {
    console.log(`Updating: ${JSON.stringify(values)}`)
    updateStatus.mutate(updatedItems, {
        onError: (error) => {
          Modal.error({
            title: `Error: ${error.response.data.message}`,
            onOk: () => updateStatus.reset(),
          });
          setDataSource(user?.data?.data?.orders)
        },
        onSuccess: (data) => {
            const result = data.data;
            const successOrders = result.success;
            const failedOrders = result.failed;
            let failedArray = [];


            if (failedOrders != null && Object.keys(failedOrders).length > 0) {
              failedArray = Object.entries(failedOrders).map(
                ([key, value]) => ({ order: key, reason: value })
              );
            }

            if((Array.isArray(successOrders) && successOrders.length > 0) || failedArray.length > 0){


               const failedCols = [
                 {
                   title: "Id",
                   render: (text, record, index) => (
                     <div>
                       <p
                         style={{ color: "darkslateblue", fontWeight: "bold" }}
                       >
                         {record.order}
                       </p>
                     </div>
                   ),
                 },

                 {
                   title: "Reason",
                   render: (text, record, index) => {
                     return <p>{record.reason}</p>;
                   },
                 },
               ];

               Modal.info({
                 title: "Result:",
                 width: "60%",
                 content: (
                   <div>
                     {Array.isArray(successOrders) &&
                     successOrders.length > 0 ? (
                       <>
                         <p
                           style={{
                             color: "green",
                             fontWeight: "bold",
                           }}
                         >
                           Success:{" "}
                         </p>
                         <List
                           header={
                             <p
                               style={{
                                 color: "darkslateblue",
                                 fontWeight: "bold",
                               }}
                             >
                               Id
                             </p>
                           }
                           bordered
                           dataSource={successOrders}
                           size="small"
                           renderItem={(id) => (
                             <List.Item
                               style={{
                                 wordWrap: "break-word",
                                 wordBreak: "break-word",
                               }}
                             >
                               {id}
                             </List.Item>
                           )}
                         ></List>
                       </>
                     ) : null}

                     {failedArray.length > 0 ? (
                       <div>
                         <p
                           style={{
                             color: "red",
                             fontWeight: "bold",
                           }}
                         >
                           Failed:
                         </p>
                         <Table
                           rowKey={(record) => record.id}
                           bordered
                           dataSource={failedArray}
                           columns={failedCols}
                         ></Table>
                       </div>
                     ) : null}
                   </div>
                 ),
                 onOk: () => updateStatus.reset(),
               });
            }

                setUpdatedItems([]);
                const updateDatasource = () => {
                    const state = queryClient.getQueryState(user_keys.current_user);

                    if (state.isInvalidated) {
                      console.log(`isInvalidated: ${state.isInvalidated}`);
                      console.log(`isFetching: ${user.isFetching}`);
                      setTimeout(updateDatasource, 250);
                    } else {
                      console.log("UPDATING");
                      setDataSource(state.data.data.orders);
                    }
                }
                updateDatasource();
        }  
    });
  }
  
  const EditableCell = ({
    title,
    editable,
    children,
    dataIndex,
    record,
    handleSave,
    ...restProps
  }) => {
    const [editing, setEditing] = useState(false);
    const inputRef = useRef(null);
    const form = useContext(EditableContext);
    useEffect(() => {
      if (editing) {
        inputRef.current.focus();
      }
    }, [editing]);

    const toggleEdit = () => {
      setEditing(!editing);
      form.setFieldsValue({
        [dataIndex]: record[dataIndex],
      });
    };

    const save = async () => {
      try {
        const values = await form.validateFields();
        toggleEdit();
        handleSave({ ...record, ...values });
      } catch (errInfo) {
        console.log("Save failed:", errInfo);
      }
    };

    let childNode = children;

    if (editable) {
      childNode = (
        <Form.Item
          style={{
            margin: 0,
          }}
          name={dataIndex}
          initialValue={record.status}
          validateTrigger={""}
          rules={[
            {
              required: true,
              message: `${title} is required.`,
            },
          ]}
        >
          <Select
            ref={inputRef}
            defaultValue={record.status}
            disabled={record.status === "CANCELED"}
            status={record.status === "CANCELED"? "error": "warning"}
            onClick={() => toggleEdit}
            onChange={(value) => {
              console.log(`VALUE: ${JSON.stringify(value)}`);
              const newArray = [...updatedItems];
              const index = newArray.findIndex(
                (element) => element.orderId === record.id
              );
              if (index > -1) {
                newArray[index].orderId = value;
              } else {
                newArray.push({
                  orderId: record.id,
                  orderStatus: value,
                });
              }
              setUpdatedItems(newArray);
              save();
              console.log(`new Array: ${JSON.stringify(newArray)}`);
              console.log(`Updated: ${JSON.stringify(updatedItems)}`);
            }}
          >
            {["ORDERED", "DELIVERYING", "DELIVERED", "CANCELED"].map(
              (state, index) => (
                <Option
                  value={state}
                  key={index}
                  disabled={state === record.status || state === "CANCELED"}
                >
                  {state}
                </Option>
              )
            )}
          </Select>
        </Form.Item>
      );
    }

    return <td {...restProps}>{childNode}</td>;
  };

  const EditableRow = ({ index, ...props }) => {
    const [form] = Form.useForm();
    return (
      <Form form={form} component={false}>
        <EditableContext.Provider value={form}>
          <tr {...props} />
        </EditableContext.Provider>
      </Form>
    );
  };

   if (user.isLoading || user.data.data == null || productList.isLoading ||productList.data?.data == null) {
     return (
       <div
         style={{
           width: "100%",
           height: 300,
           textAlign: "center",
           padding: 100,
         }}
       >
         <Spin tip="Loading..." size="large"></Spin>
       </div>
     );
   }

    if (user.isError) {
      return (
        <Result
          status={"error"}
          title="ERROR"
          subTitle="Sorry, some error occurred. Please try again later"
          extra={
            <Link to={"/"}>
              <Button type="primary">Back Home</Button>
            </Link>
          }
        />
      );
    }

 if(user.isSuccess && productList.isSuccess){
        
            const columns = [
              {
                title: "Code",
                className: "tableCell",
                dataIndex: "id",
                width: 110,
                render: (text, record) => (
                  <div
                    style={{ wordWrap: "break-word", wordBreak: "break-word" }}
                  >
                    {text}
                  </div>
                ),
              },

              {
                title: "Status",
                dataIndex: "status",
                width: 120,
                className: "tableCell",
                editable: true,
              },

              {
                title: "Avatar",
                width: 110,
                responsive: ["sm"],
                render: (text, record, index) => (
                  <Avatar
                    size={{ xs: 50, sm: 50, md: 80, lg: 80, xl: 80, xxl: 80 }}
                    shape={"square"}
                    src={
                      record.choice != null
                        ? record.choice.imageUrl
                        : productUtil.findProduct(
                            record.product,
                            productList.data.data
                          ).avatarImageURL
                    }
                  />
                ),
              },

              {
                title: "Name",
                width: 150,
                render: (text, record, index) => (
                  <div>
                    <p>
                      <span
                        style={{ color: "darkslateblue", fontWeight: "bold" }}
                      >
                        {
                          productUtil.findProduct(
                            record.product,
                            productList.data.data
                          ).name
                        }
                      </span>
                      {Array.isArray(record.choices) &&
                      record.choices.length > 0
                        ? record.choices.map((choice) => (
                            <span
                              key={choice.id}
                              style={{ fontWeight: "bold" }}
                            >
                              {` - ${choice.characteristic}: ${choice.name}`}
                            </span>
                          ))
                        : null}
                    </p>
                  </div>
                ),
              },

              /*               {
                title: "Unit Price",
                render: (text, record, index) => {
                  const price =
                    record.choice != null
                      ? record.choice.unitPrice
                      : productUtil.findProduct(
                          record.product,
                          productList.data.data
                        ).defaultUnitPrice;

                  return Formatter.addNumberSeparator(price.toString());
                },
              }, */

              {
                title: "Quantity",
                dataIndex: "quantity",
                responsive: ["sm"],
                width: 90,
                align: "center",
              },

              {
                title: "Delivery fee",
                dataIndex: "deliveryFeePerUnit",
                responsive: ["sm"],
                render: (text, record, index) => {
                  const deliveryFeePerUnit = productUtil.findProduct(
                    record.product,
                    productList.data.data
                  ).deliveryFeePerUnit;

                  const maxDeliveryFee = productUtil.findProduct(
                    record.product,
                    productList.data.data
                  ).maxDeliveryFee;

                  const DeliveryFee =
                    deliveryFeePerUnit * record.quantity < maxDeliveryFee
                      ? deliveryFeePerUnit
                      : maxDeliveryFee;

                  return Formatter.addNumberSeparator(DeliveryFee.toString());
                },
              },

              {
                title: "Total Price",
                width: 100,
                render: (text, record, index) => {
                  return Formatter.addNumberSeparator(
                    record.finalPrice.toString()
                  );
                },
              },

              {
                title: "Order date",
                dataIndex: "orderDate",
                responsive: ["sm"],
                width: 145,
              },

              {
                title: "Delivery date",
                dataIndex: "deliveryDate",
                responsive: ["sm"],
              },
            ];

            const handleSave = (row) => {
                 console.log(`NEW ROW: ${JSON.stringify(row)}`);
                 const newData = [...dataSource];
                 const index = newData.findIndex((item) => row.id === item.id);
                 const item = newData[index];
                 newData.splice(index, 1, { ...item, ...row });
                 scrollPosition.current = window.pageYOffset;
                 setDataSource(newData);
            };

            const editedColumns = columns.map((col) => {
                  if (!col.editable) {
                    return col;
                  }

                  return {
                    ...col,
                    onCell: (record) => ({
                      record,
                      editable: col.editable,
                      dataIndex: col.dataIndex,
                      title: col.title,
                      handleSave: handleSave,
                    }),
                  };
            });

              const EditableTable = (props) => {
                return (
                  <Table
                    components={{
                      body: { row: EditableRow, cell: EditableCell },
                    }}
                    /*  rowClassName={() => "editable-row"} */
                    rowKey={(record) => record.id}
                    bordered
                    dataSource={props.dataSource}
                    columns={editedColumns}
                    scroll={{ x: 500 }}
                    footer={() => (
                      <Space align="center">
                        <Button
                          type="primary"
                          danger
                          onClick={() => {
                            handleUpdate(updatedItems);
                          }}
                          disabled={
                            !Array.isArray(updatedItems) ||
                            updatedItems.length === 0
                          }
                        >
                          Update orders status
                        </Button>
                      </Space>
                    )}
                  />
                );
              };

    return (
      <Spin spinning = {updateStatus.isLoading} size={'large'} tip='Updating orders...'>
        <EditableTable dataSource = {dataSource}></EditableTable>
      </Spin>
    );
 }
}
