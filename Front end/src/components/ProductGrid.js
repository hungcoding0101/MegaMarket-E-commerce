import { Col, Row } from 'antd';
import React from 'react'
import Product from './Product';

export default function ProductGrid(props) {

  const {source} = props;
  console.log(`HERE: SOURCE: ${JSON.stringify(source)}`);

  if(source != null){ 
    return (
      <Row
        gutter={[{ xs: 1, sm: 1, md: 3, lg: 3, xl: 3, xxl: 3 }, 3]}
        align="stretch"
      >
        {source.map((product) => (
          <Col
            xs={12}
            sm={8}
            md={6}
            lg={6}
            xl={6}
            xxl={6}
            style={{ height: 280 }}
          >
            <Product key={product._id} product={product}></Product>
          </Col>
        ))}
      </Row>
    );
  }
}
