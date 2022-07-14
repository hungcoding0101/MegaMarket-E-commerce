import { Result, Skeleton } from "antd";
import React from 'react'
import CategoryGrid from '../components/CategoryGrid';
import { useFetchCategoryByDepth } from '../Hooks/ProductCategoryHooks';


export default function HomeScreen() {
   const product_categories = useFetchCategoryByDepth(2, 0, 20, null, null, true);

      if (
        product_categories.isLoading ||
        product_categories.data?.data == null
      ) {
        return (
          <Skeleton style={{height: 500}} active={true} loading={true}></Skeleton>
        );
      }

      if(product_categories.isError){
          return (
                    <Result
                      status={"error"}
                      title="Error"
                      subTitle="Sorry, some errors occurred. We will be back soon!"
                    />
                  );
      }

      if (
        product_categories.isSuccess &&
        product_categories.data.data != null
      ) {

        return (
          <div>
            <CategoryGrid source={product_categories.data.data}></CategoryGrid>
          </div>
        );
      }
}
    
    