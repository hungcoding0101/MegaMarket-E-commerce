import { Avatar, Button, Spin, Table } from 'antd';
import React, { useState } from 'react'
import { useOwnedProducts } from '../Hooks/SellerHooks';
import { useCurrentUser } from '../Hooks/UserHooks';
import { Formatter } from '../Util/Formatter';
import { productUtil } from '../Util/ProductUtil';

const SellerProducts = () => {
    const user = useCurrentUser(null, null, true);

    const [dataSource, setDataSource] = useState(
      user.isSuccess ? user.data.data.ownedProducts : []
    );

    const productList = useOwnedProducts(null, null, true);

    if(productList.isLoading || productList.data.data == null){
      return <Spin tip={'Loading...'}></Spin>
    }
         

   if(productList.isSuccess && user.isSuccess){

    const columns = [
      {
        title: "Id",
        render: (_, record) => (
          <div style={{ wordWrap: "break-word", wordBreak: "break-word" }}>
            {record}
          </div>
        ),
      },

      {
        title: "Avatar",
        render: (text, record, index) => (
          <Avatar
            size={{ xs: 50, sm: 50, md: 80, lg: 80, xl: 80, xxl: 80 }}
            shape={"square"}
            src={
              productUtil.findProduct(record.toString(), productList.data.data)
                .avatarImageURL
            }
          />
        ),
      },

      {
        title: "Name",
        render: (text, record, index) => {
          const product = productUtil.findProduct(
            record,
            productList.data.data
          );
          console.log(
            `RECORD: ${record.toString()} || TEXT: ${text.toString()} || PRODUCT: ${JSON.stringify(
              product
            )}`
          );
          return (
            <div>
              <p>
                <span style={{ color: "darkslateblue", fontWeight: "bold" }}>
                  {product.name}
                </span>
              </p>
            </div>
          );
        },
      },

      {
        title: "Unit Price",
        render: (text, record, index) => {
          const price = productUtil.findProduct(
            record,
            productList.data.data
          ).defaultUnitPrice;
          return Formatter.addNumberSeparator(price.toString());
        },
      },

      {
        title: "Modify",
        key: "modify",
        render: (_, record) => (
          /* user.data.data.cart.length > 0 ? ( */
          <Button type="primary" onClick={() => {}}>
            Modify
          </Button>
        ),
      },
    ];
    return (
      <Table
        rowKey={(record) => record}
        bordered
        dataSource={dataSource}
        columns={columns}
        scroll={{ x: 800 }}
      />
    );
  }
}

export default SellerProducts;