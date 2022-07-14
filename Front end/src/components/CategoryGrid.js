import { Col, Row } from "antd";
import React from "react";
import ProductCategory from "./ProductCategory";
  
export default function CategoryGrid(props) {
  const { source } = props;

  if (source != null) {
    return (
      <Row
        gutter={[{ xs: 1, sm: 1, md: 4, lg: 4, xl: 4, xxl: 4 }, 4]}
        align="stretch"
      >
        {source.map((category) => (
          <Col
            xs={24}
            sm={12}
            md={12}
            lg={8}
            xl={8}
            xxl={8}
            style={{ height: 280 }}
          >
            <ProductCategory
              key={category._id}
              category={category}
            ></ProductCategory>
          </Col>
        ))}
      </Row>
    );
  }
}
