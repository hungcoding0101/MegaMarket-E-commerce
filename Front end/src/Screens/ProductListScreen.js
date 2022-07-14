import { Affix, Skeleton, Button, Checkbox, Col, Divider, Drawer, Empty, Pagination, Radio, Rate, Result, Row, Space, Tabs } from "antd";
import React, { useState } from "react";
import { Redirect, useHistory } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { useSearch_product, } from "../Hooks/ProductHooks";
import  ProductGrid  from "../components/ProductGrid";
import { useParams } from "react-router-dom";
import { PRODUCT_SEARCH_TYPES } from "../constants/ProductConstant";
import {
  StarOutlined,
  CrownOutlined,
  ShopOutlined,
  FilterOutlined,
  CloseCircleOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined,
} from "@ant-design/icons";
import Card from "antd/lib/card/Card";


const ProductListScreen = () => {
  console.log(`URL: ${window.location.href}`)
        const history = useHistory();
        const request_params = new URLSearchParams(useLocation().search);
        const URL_params = new useParams();
        const searchType = URL_params.searchType;
        const keyWords = decodeURIComponent(request_params.get("keyWords"))

        const encoded_BRAND = request_params.get("BRAND");
        const encoded_SELLER = request_params.get("SELLER");

        let RATING_SCORE = request_params.get("RATING_SCORE");
        let BRAND = encoded_BRAND != null ? decodeURIComponent(encoded_BRAND).split(",") : [];
        let SELLER = encoded_SELLER != null ? decodeURIComponent(encoded_SELLER).split(",") : [];
        let sort_type = request_params.get("sort");

        let offset = Number.isNaN(parseInt(request_params.get("offset")))
          ? 0
          : parseInt(request_params.get("offset")); ;
        const limit = 20;
        const [filterVisible, setFilterVisible] = useState(false);

      const products = useSearch_product(
        searchType,
        keyWords,
        sort_type,
        {
          RATING_SCORE: [],
          BRAND: [],
          SELLER: [],
        },
        0,
        limit,
        null,
        null,
        PRODUCT_SEARCH_TYPES.includes(searchType)
      );

      const filtered_products = useSearch_product(
        searchType,
        keyWords,
        sort_type,
        {
          RATING_SCORE: RATING_SCORE != null ? [RATING_SCORE] : [],
          BRAND: BRAND,
          SELLER: SELLER,
        },
        offset,
        limit,
        null,
        null,
        PRODUCT_SEARCH_TYPES.includes(searchType) && products.isSuccess
      );



         if (!PRODUCT_SEARCH_TYPES.includes(searchType)) {
           return <Redirect to={"*"}></Redirect>;
         }

         if(products.isLoading || products.data.data == null){
                 return (
                   <Skeleton active={true} loading={true}></Skeleton>
                 );
         }

         if(products.isError){
                  return (
                    <Result
                      status={"error"}
                      title="Error"
                      subTitle="Sorry, some errors occurred. We will be back soon!"
                    />
                  );
         }

         if (products.data.data.products.length < 1){
            return (
              <Empty
                description={<span>No product found for this category</span>}
              ></Empty>
            );    
         }

        if(products.isSuccess){

            const handleFilter = (thisOffset) => {
                let query = `/product/list/${searchType}?keyWords=${encodeURIComponent(
                    keyWords
                  )}`
                
                  if(RATING_SCORE != null){
                      query = query.concat(`&RATING_SCORE=${RATING_SCORE}`);
                  }

                  if(Array.isArray(BRAND) && BRAND.length > 0){
                      query = query.concat(`&BRAND=${encodeURIComponent(BRAND.join())}`);
                  }

                  if(Array.isArray(SELLER) && SELLER.length > 0){
                      query = query.concat(`&SELLER=${encodeURIComponent(SELLER.join())}`);
                  }

                  if(sort_type != null){
                      query = query.concat(`&sort=${sort_type}`);
                  }

                      query = query.concat(
                        `&offset=${thisOffset != null ? thisOffset : 0}&limit=${limit}`
                      );
                

                  history.push(query);
            }

            const FilterMenu = (props) => {
                return (
                  <>
                    <Card
                      title={
                        <span style={{ color: "darkblue" }}>
                          <StarOutlined />{" "}
                          <span style={{ fontWeight: "bold" }}>
                            Rating score
                          </span>
                        </span>
                      }
                      bodyStyle={{ padding: 0 }}
                    >
                      <Radio.Group
                        value={RATING_SCORE}
                        onChange={(e) => {
                          RATING_SCORE = e.target.value;
                          handleFilter();
                        }}
                      >
                        {["5", "4", "3", "2", "1"].map((num) => (
                          <Radio
                            value={num}
                            onClick={(e) => {
                              if (RATING_SCORE === e.target.value) {
                                e.target.checked = false;
                                RATING_SCORE = null;
                                handleFilter();
                              }
                            }}
                          >
                            <>
                              <Rate
                                value={num}
                                disabled
                                style={{
                                  margin: 0,
                                  width: 135,
                                  fontSize: "1.5rem",
                                  alignSelf: "center",
                                  padding: 0,
                                  maxHeight: 8,
                                }}
                              ></Rate>
                              <span style={{ fontWeight: "bold" }}>
                                {num} scores or more
                              </span>
                            </>
                          </Radio>
                        ))}
                      </Radio.Group>
                    </Card>
                    <Divider></Divider>
                    <Card
                      title={
                        <span style={{ color: "darkblue" }}>
                          <CrownOutlined />{" "}
                          <span style={{ fontWeight: "bold" }}>Brand</span>
                        </span>
                      }
                      bodyStyle={{ padding: 0 }}
                      extra={
                        <Button
                          size="small"
                          danger
                          type="primary"
                          onClick={() => {
                            BRAND = [];
                            handleFilter();
                          }}
                        >
                          Uncheck all
                        </Button>
                      }
                    >
                      <Checkbox.Group
                        value={BRAND}
                        onChange={(values) => {
                          console.log(`CHECKED BRANDS: ${values}`);
                          BRAND = values;
                          handleFilter();
                        }}
                      >
                        <Space direction="vertical">
                          {Object.keys(products.data.data.brands).map(
                            (brand) => (
                              <Checkbox value={brand}>{brand}</Checkbox>
                            )
                          )}
                        </Space>
                      </Checkbox.Group>
                    </Card>

                    <Divider></Divider>

                    <Card
                      title={
                        <span style={{ color: "darkblue" }}>
                          <ShopOutlined />{" "}
                          <span style={{ fontWeight: "bold" }}>Seller</span>
                        </span>
                      }
                      bodyStyle={{ padding: 0 }}
                      extra={
                        <Button
                          size="small"
                          danger
                          type="primary"
                          onClick={() => {
                            SELLER = [];
                            handleFilter();
                          }}
                        >
                          Uncheck all
                        </Button>
                      }
                    >
                      <Checkbox.Group
                        value={SELLER}
                        onChange={(values) => {
                          SELLER = values;
                          handleFilter();
                        }}
                      >
                        <Space direction="vertical">
                          {Object.keys(products.data.data.sellers).map(
                            (seller) => (
                              <Checkbox value={seller}>{seller}</Checkbox>
                            )
                          )}
                        </Space>
                      </Checkbox.Group>
                    </Card>
                  </>
                );
                
            }
           return (
             <Row
               gutter={[{ xs: 1, sm: 1, md: 4, lg: 4, xl: 4, xxl: 4 }, 4]}
               align="stretch"
             >
               <Col xs={0} sm={0} md={0} lg={6} xl={6} xxl={6}>
                 <Drawer
                   title={<div style={{ marginLeft: 30 }}>Filters</div>}
                   width={310}
                   placement="left"
                   visible={filterVisible}
                   closable={false}
                   onClose={() => setFilterVisible(false)}
                   headerStyle={{ padding: 5 }}
                   bodyStyle={{ padding: 0}}
                   extra={
                     <Button
                       onClick={() => setFilterVisible(false)}
                       type="primary"
                       shape="circle"
                       size="large"
                     >
                       <CloseCircleOutlined></CloseCircleOutlined>
                     </Button>
                   }
                 >
                   <FilterMenu></FilterMenu>
                 </Drawer>
                 <FilterMenu></FilterMenu>
               </Col>
               <Col xs={24} sm={24} md={24} lg={18} xl={18} xxl={18}>
                 <Space>
                   <h1 style={{ margin: "2rem", fontWeight: "bold" }}>
                     {keyWords}
                   </h1>
                 </Space>
                 <Affix>
                   <Tabs
                     tabBarStyle={{
                       backgroundColor: "white",
                     }}
                     tabBarGutter={24}
                     defaultActiveKey={sort_type}
                     onChange={(key) => {
                       sort_type = key;
                       handleFilter();
                       console.log(`SORT: ${sort_type}`);
                     }}
                     tabBarExtraContent={{
                       left: (
                         <Button
                           type="text"
                           style={{ fontWeight: "bold" }}
                           onClick={() => setFilterVisible(!filterVisible)}
                         >
                           <FilterOutlined></FilterOutlined> Filter
                         </Button>
                       ),
                     }}
                   >
                     <Tabs.TabPane
                       key={null}
                       tab={<span style={{ fontWeight: "bold" }}>Name</span>}
                     ></Tabs.TabPane>

                     <Tabs.TabPane
                       key={"PRICE_ASC"}
                       tab={
                         <span style={{ fontWeight: "bold" }}>
                           Price <ArrowUpOutlined />
                         </span>
                       }
                     ></Tabs.TabPane>

                     <Tabs.TabPane
                       key={"PRICE_DESC"}
                       tab={
                         <span style={{ fontWeight: "bold" }}>
                           Price <ArrowDownOutlined />
                         </span>
                       }
                     ></Tabs.TabPane>

                     <Tabs.TabPane
                       key={"RATING"}
                       tab={
                         <span style={{ fontWeight: "bold" }}>
                           Rating score
                         </span>
                       }
                     ></Tabs.TabPane>

                     <Tabs.TabPane
                       key={"SALES"}
                       tab={<span style={{ fontWeight: "bold" }}>Sales</span>}
                     ></Tabs.TabPane>
                   </Tabs>
                 </Affix>
                 {(filtered_products.isLoading ||
                   filtered_products.data?.data == null) && (
                   <Skeleton active={true} loading={true}></Skeleton>
                 )}

                 {filtered_products.isError && (
                   <Result
                     status={"error"}
                     title="Error"
                     subTitle="Sorry, some errors occurred. We will be back soon!"
                   />
                 )}

                 {filtered_products.isSuccess && (
                   <>
                     {filtered_products.data?.data?.products.length < 1 ? (
                       <Empty
                         description={
                           <span>No product matches your requirements</span>
                         }
                       ></Empty>
                     ) : (
                       <>
                         <ProductGrid
                           source={filtered_products.data.data.products}
                         ></ProductGrid>
                         <Pagination
                           total={filtered_products.data?.data?.matchTotal}
                           pageSize={limit}
                           current={offset / limit + 1}
                           onChange={(page) => {
                             handleFilter(limit * (page - 1));
                           }}
                         ></Pagination>
                       </>
                     )}
                   </>
                 )}
               </Col>
             </Row>
           );
        }
}

export default ProductListScreen;