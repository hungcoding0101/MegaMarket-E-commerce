import { Avatar, Button, Modal, Table, Skeleton, Result } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import React, { useState } from 'react'
import { productUtil } from "../Util/ProductUtil";
import { Formatter } from "../Util/Formatter";
import { useCurrentUser } from '../Hooks/UserHooks';
import { Link } from 'react-router-dom';
import { useProductsById } from '../Hooks/ProductHooks';
import { useCancelOrder } from '../Hooks/OrderHooks';


export default function Orders() {

      const {
        data: user_data,
        isError: user_isError,
        isLoading: user_isLoading,
        isSuccess: user_isSuccess,
      } = useCurrentUser(null, null, false);

      const productIds = user_data?.data?.orders.map((order) => order.product);

      const {
        data: all_product_data,
        isError: all_product_isError,
        isLoading: all_product_isLoading,
        isSuccess: all_product_isSuccess,
      } = useProductsById(productIds, null, null, true);

      const cancel = useCancelOrder();

      const [dataSource, setDataSource] = useState(user_data?.data?.orders);

      if (
        user_isLoading ||
        all_product_isLoading ||
        user_data.data == null ||
        all_product_data.data == null
      ) {
        return (
          <Skeleton active={true} loading={true}></Skeleton>
        );
      }

      if (user_isError || all_product_isError) {
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

      if (user_isSuccess) {
        const handleCancel = async (id) => {
          cancel.mutate(id, {
            onError: (error) =>
              Modal.error({
                title: `ERROR: ${error.response.data.message}`,
                onOk: () => cancel.reset(),
              }),
            onSuccess: () => {
              const newDataSource = [...dataSource];
              const index = newDataSource.findIndex((order) => order.id === id);
              newDataSource[index].status = "CANCELED";
              setDataSource(newDataSource);
              console.log(`NEW SOURCE: ${JSON.stringify(dataSource)}`);

              Modal.success({
                title: `Cancelled the order: ${id}`,
                onOk: () => cancel.reset(),
              });
            },
          });
        };

        const columns = [
          {
            title: "Code",
            dataIndex: "id",
            width: 100,
            render: (text, record) => (
              <div style={{ wordWrap: "break-word", wordBreak: "break-word" }}>
                {text}
              </div>
            ),
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
                        all_product_data.data
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
            title: "Status",
            dataIndex: "status",
            width: 110,
            filters: [
              ...new Set(user_data.data.orders.map((item) => item.status)),
            ].map((status) => ({ text: status, value: status })),

            onFilter: (value, record) => record.status === value,

            render: (_, record) => {
              let color = "";
              switch (record.status) {
                case "CANCELED":
                  color = "red";
                  break;

                case "DELIVERED":
                  color = "blue";
                  break;

                default:
                  color = "black";
                  break;
              }
              return <p style={{ color: color }}>{record.status}</p>;
            },
          },

          {
            title: "Seller",
            width: 120,
            responsive: ["sm"],
            filters: [
              ...new Set(
                user_data.data.orders.map(
                  (item) =>
                    productUtil.findProduct(item.product, all_product_data.data)
                      .seller.username
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
            responsive: ["sm"],
            render: (text, record, index) => {
              const price =
                record.choice != null
                  ? record.choice.unitPrice
                  : productUtil.findProduct(
                      record.product,
                      all_product_data.data
                    ).defaultUnitPrice;

              return Formatter.addNumberSeparator(price.toString());
            },
          },

          {
            title: "Quantity",
            dataIndex: "quantity",
            width: 90,
            align: "center",
          },

          {
            title: "Delivery fee",
            responsive: ["sm"],
            dataIndex: "deliveryFeePerUnit",
            render: (text, record, index) => {
              const deliveryFeePerUnit = productUtil.findProduct(
                record.product,
                all_product_data.data
              ).deliveryFeePerUnit;

              const maxDeliveryFee = productUtil.findProduct(
                record.product,
                all_product_data.data
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
            dataIndex: "finalPrice",
            render: (text, record, index) => {
              return Formatter.addNumberSeparator(record.finalPrice.toString());
            },
          },

          {
            title: "Order date",
            responsive: ["sm"],
            dataIndex: "orderDate",
          },

          {
            title: "Delivery date",
            dataIndex: "deliveryDate",

          },

          {
            title: "Cancel",
            key: "cancel",
            width: 80,
            render: (_, record) =>
              record.status !== "CANCELED" ? (
                <Button
                  type="primary"
                  danger
                  onClick={() =>
                    Modal.confirm({
                      onOk: () => handleCancel(record.id),
                      title: "Are you sure to cancel?",
                    })
                  }
                >
                  <DeleteOutlined></DeleteOutlined>
                </Button>
              ) : null,
          },
        ];

        return (
          <Table
            tableLayout="fixed"
            rowKey={(record) => record.id}
            bordered
            dataSource={dataSource}
            columns={columns}
            /* rowSelection={{
              type: "checkbox",
              onChange: (selectedRowKeys, selectedRows) => {},
            }} */
            scroll={{ x: 800, y: 400 }}
          />
        );
      }
}
