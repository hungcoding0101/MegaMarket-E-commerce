import React, { useContext, useEffect, useRef, useState, memo } from 'react'
import { Form,  Button, InputNumber, Avatar, Table, Modal, Spin, Skeleton } from 'antd';
import { DeleteOutlined } from "@ant-design/icons";
import {productUtil} from '../Util/ProductUtil'
import { Formatter } from "../Util/Formatter";
import PlaceOrderForm from '../components/placeOrderForm';
import { useCurrentUser } from '../Hooks/UserHooks';
import { useAllProducts } from '../Hooks/ProductHooks';
import { usePlaceOrder } from '../Hooks/OrderHooks';
import { useDeleteCart } from '../Hooks/CustomerHooks'


const  CartScreen = () => {

  const { data:user_data, isError: user_isError, isLoading: user_isLoading, isSuccess: user_isSuccess } =
    useCurrentUser(null, null, false);

  const {
    data: all_product_data,
    isError: all_product_isError,
    isLoading: all_product_isLoading,
    isSuccess: all_product_isSuccess,
  } = useAllProducts(
    null,
    (error) => {
      console.log(`ERROR: ${JSON.stringify(error.response.data)}`);
    },
    true
  );

  const deleteCart = useDeleteCart();

  const placeOrder = usePlaceOrder();

  const [dataSource, setDataSource] = useState(user_data.data.cart);

  const [visible, setVisible] = useState(false);

  const EditableContext = React.createContext(null);

  const scrollPosition = useRef(0);

  let selectedItems = [];

  const [orders, setOrders] = useState([])

 useEffect(() => window.scrollTo(0, scrollPosition), [scrollPosition])
 
  if(user_isLoading || all_product_isLoading ){
    return (
      <Skeleton active={true} loading={true}/>
    );
  }

    if (user_isError || all_product_isError) {
      return Modal.error({ title: "Some error occurred"});
    }

    
   if(user_isSuccess || all_product_isSuccess){
      console.log(`HERE: ALL: ${JSON.stringify(all_product_data.data)}`);

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
          childNode = editing ? (
            <Form.Item
              style={{
                margin: 0,
              }}
              name={dataIndex}
              initialValue={record.quantity}
              validateTrigger={""}
              rules={[
                {
                  required: true,
                  message: `${title} is required.`,
                },

                ({ getFieldValue }) => ({
                  validator(_, value) {
                    const product = productUtil.findProduct(
                      record.product,
                      all_product_data.data
                    );
                    const stock =
                      record.choice != null
                        ? record.choice.stock
                        : product.totalStock;
                    if (value > stock) {
                      Modal.error({
                        title: `There remains only ${stock} items for this product`,
                      });
                      form.resetFields();
                      return Promise.reject();
                    }
                    return Promise.resolve();
                  },
                }),
              ]}
            >
              <InputNumber
                ref={inputRef}
                onPressEnter={save}
                onBlur={save}
                formatter={(value) => Formatter.addNumberSeparator(value)}
                parser={(value) => Formatter.getNumberRawValue(value)}
              />
            </Form.Item>
          ) : (
            <Button type="primary" size="middle" onClick={toggleEdit} ghost>
              {children}
            </Button>
          );
        }

        return <td {...restProps}>{childNode}</td>;
      };

      const handleDelete = (id) => {
        deleteCart.mutate(id, {
          onSuccess: (data) => {
            setDataSource(dataSource.filter((item) => item.id !== id));
            Modal.success({
              title: "Deleted!",
              onOk: () => deleteCart.reset(),
            });
          },

          onError: (error) =>
            Modal.error({
              title: `Error: ${error.response.data.message}`,
              onOk: () => deleteCart.reset(),
            }),
        });
      };

      const handleSave = (row) => {
        console.log(`NEW ROW: ${JSON.stringify(row)}`);
        const newData = [...dataSource];
        const index = newData.findIndex((item) => row.id === item.id);
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        scrollPosition.current = window.pageYOffset;
        setDataSource(newData);
      };

      const columns = [
        {
          title: "Avatar",
          render: (text, record, index) => (
            <Avatar
              size={{ xs: 50, sm: 50, md: 80, lg: 80, xl: 80, xxl: 80 }}
              shape={"square"}
              src={
                record.choice != null
                  ? record.choice.imageUrl
                  : productUtil.findProduct(
                      record.product,
                      all_product_data.data
                    ).avatarImageURL
              }
            />
          ),
        },

        {
          title: "Name",
          render: (text, record, index) => (
            <div>
              <p>
                <span style={{ color: "darkslateblue", fontWeight: "bold" }}>
                  {
                    productUtil.findProduct(
                      record.product,
                      all_product_data.data
                    ).name
                  }
                </span>
                {Array.isArray(record.choices) && record.choices.length > 0
                  ? record.choices.map((choice) => (
                      <span key={choice.id} style={{ fontWeight: "bold" }}>
                        {` - ${choice.characteristic}: ${choice.name}`}
                      </span>
                    ))
                  : null}
              </p>
            </div>
          ),
        },

        {
          title: "Seller",
          responsive: ["md"],
          filters: [
            ...new Set(
              user_data.data?.cart.map(
                (item) =>
                  productUtil.findProduct(item.product, all_product_data.data)
                    ?.seller.username
              )
            ),
          ].map((seller) => ({ text: seller, value: seller })),
          onFilter: (value, record) =>
            productUtil.findProduct(record.product, all_product_data.data)
              .seller.username === value,
          render: (text, record, index) =>
            productUtil.findProduct(record.product, all_product_data.data)
              .seller.username,
        },

        {
          title: "Unit Price",
          render: (text, record, index) => {
            return Formatter.addNumberSeparator(record.unitPrice.toString());
          },
        },

        {
          title: "Quantity",
          dataIndex: "quantity",
          editable: true,
          align: "center",
          /* shouldCellUpdate: (record, prevRecord) => record.quantity !== prevRecord.quantity, */
        },

        {
          title: "Delivery fee",
          key: 'deliveryFee',
          render: (text, record, index) => {
            const deliveryFeePerUnit = record.deliveryFeePerUnit;
            const maxDeliveryFee = record.maxDeliveryFee;
            const finalFee = deliveryFeePerUnit * record.quantity < maxDeliveryFee ? deliveryFeePerUnit * record.quantity
            :maxDeliveryFee
            return Formatter.addNumberSeparator(
              finalFee.toString()
            );
          },
        },

/*         {
          title: "Max delivery fee",
          dataIndex: "maxDeliveryFee",
          render: (text, record, index) => {
            return Formatter.addNumberSeparator(
              record.maxDeliveryFee.toString()
            );
          },
        }, */

        {
          title: "Total Price",
          key: "totalPrice",
          render: (text, record, index) => {
            const unitPrice = record.unitPrice;
            const quantity = record.quantity;
            const deliveryFeePerUnit = record.deliveryFeePerUnit;
            const maxDeliveryFee = record.maxDeliveryFee;
            const deliveryFee =
              deliveryFeePerUnit * record.quantity < maxDeliveryFee
                ? deliveryFeePerUnit * record.quantity
                : maxDeliveryFee;
            return Formatter.addNumberSeparator((unitPrice*quantity+deliveryFee).toString());
          },
        },

        {
          title: "Delete",
          key: "delete",
          render: (_, record) => (
            <Button
              type="primary"
              danger
              onClick={() =>
                Modal.confirm({
                  onOk: () => handleDelete(record.id),
                  title: "Are you sure to delete?",
                })
              }
            >
              <DeleteOutlined></DeleteOutlined>
            </Button>
          ),
        },
      ];

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
            components={{ body: { row: EditableRow, cell: EditableCell } }}
            /*  rowClassName={() => "editable-row"} */
            rowKey={(record) => record.id}
            bordered
            dataSource={props.dataSource}
            columns={editedColumns}
            rowSelection={{
              type: "checkbox",
              onChange: (selectedRowKeys, selectedRows) => {
                selectedItems = selectedRows;
              },
            }}
            scroll={{ x: 800 }}
            footer={() => (
              <Button
                type="primary"
                danger
                onClick={() => {
                  setOrders(selectedItems);
                  setVisible(true);
                }}
                disabled={
                  !Array.isArray(selectedItems) || selectedItems.length === 0
                }
              >
                Place Order
              </Button>
            )}
          />
        );
      };

      const submit = async (values) => {
        const finalOrders = [];

        values.orders.forEach((order, index, orders) => {
          finalOrders.push({
            cartId: order.id,
            deliveryAddress: values.address,
            paymentMethod: values.paymentMethods.get(order.id),
            quantity: order.quantity,
            customerName: user_data.data?.username,
          });
        });

        placeOrder.mutate(finalOrders, {
          onSuccess: (data) => {
            console.log(JSON.stringify(values.orders));
            const result = data.data;
            const successOrders = result.successOrders;
            const failedOrders = result.failed;
            let successArray = [];
            let successCarts = [];
            let failedArray = [];
            let failedCarts = [];

            if (
              successOrders != null &&
              Object.keys(successOrders).length > 0
            ) {
              successArray = Object.entries(successOrders).map(
                ([key, value]) => ({ cart: key, order: value })
              );

              successCarts = successArray.map((item) => item.cart);

              setDataSource(
                dataSource.filter(
                  (item) => !successCarts.includes(item.id.toString())
                )
              );
            }

            if (failedOrders != null && Object.keys(failedOrders).length > 0) {
              failedArray = Object.entries(failedOrders).map(
                ([key, value]) => ({
                  cart: key,
                  reason: value,
                })
              );
              failedCarts = failedArray.map((item) => item.cart);
            }

            if (successArray.length > 0 || failedArray.length > 0) {
              const nameCol = {
                title: "Name",
                render: (text, record, index) => (
                  <div>
                    <p>
                      <span
                        style={{ color: "darkslateblue", fontWeight: "bold" }}
                      >
                        {
                          productUtil.findProduct(
                            record.product,
                            all_product_data.data
                          ).name
                        }
                      </span>
                      {record.choice != null ? (
                        <span>
                          - {record.choice.characteristic}: {record.choice.name}
                        </span>
                      ) : null}
                    </p>
                  </div>
                ),
              };

              const quantityCol = {
                title: "Quantity",
                dataIndex: "quantity",
                align: "center",
                responsive: ["md"],
              };

              const successCols = [nameCol, quantityCol];

              const failedCols = [
                nameCol,
                quantityCol,
                {
                  title: "Reason",
                  render: (text, record, index) => {
                    const theFailed = failedArray.filter(
                      (item) => item.cart === record.id.toString()
                    )[0].reason;

                    return (
                      <p>
                        {theFailed.changedFieldName} {theFailed.reason}
                      </p>
                    );
                  },
                },
              ];

              Modal.info({
                title: "Result:",
                width: "100%",
                bodyStyle: { padding: 0 },
                content: (
                  <div>
                    {successArray.length > 0 ? (
                      <div>
                        <span style={{ fontWeight: "bold", color: "blue" }}>
                          Success:
                        </span>
                        <Table
                          rowKey={(record) => record.id}
                          bordered
                          dataSource={dataSource.filter((item) =>
                            successCarts.includes(item.id.toString())
                          )}
                          columns={successCols}
                        ></Table>
                      </div>
                    ) : null}

                    {failedArray.length > 0 ? (
                      <div>
                        <span style={{ fontWeight: "bold", color: "red" }}>
                          Failed:
                        </span>
                        <Table
                          rowKey={(record) => record.id}
                          bordered
                          dataSource={dataSource.filter((item) =>
                            failedCarts.includes(item.id.toString())
                          )}
                          columns={failedCols}
                        ></Table>
                      </div>
                    ) : null}
                  </div>
                ),
              });
            }
          },

          onError: (error) => {
            Modal.error({
              title: "Failed",
              content: error.response.data.message,
            });
          },
        });
      };

      console.log("READY");
      return (
        <Spin spinning={placeOrder.isLoading || deleteCart.isLoading} tip='Processing...'>
          <EditableTable dataSource={dataSource}></EditableTable>
          <Form.Provider
            onFormFinish={(name, { values, forms }) => {
              if (name === "placeOrderForm") {
                console.log("placeOrderForm SUBMITED");
                setVisible(false);
                const { placeOrderForm } = forms;
                submit(placeOrderForm.getFieldsValue(true));
                placeOrderForm.resetFields(["orders", "paymentMethods"]);
              }
            }}
          >
            <PlaceOrderForm
              visible={visible}
              onCancel={() => setVisible(false)}
              orders={orders}
            ></PlaceOrderForm>
          </Form.Provider>
        </Spin>
      );
   }
}

export default memo(CartScreen);
    