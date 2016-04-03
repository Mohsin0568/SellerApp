package com.example.mohmurtu.registration.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohmurtu.registration.ActivityHome;
import com.example.mohmurtu.registration.AddNewProduct;
import com.example.mohmurtu.registration.ProductDetailsFragment;
import com.example.mohmurtu.registration.R;
import com.example.mohmurtu.registration.imagesUtil.ImageCache;
import com.example.mohmurtu.registration.imagesUtil.ImageInternalFetcher;
import com.example.mohmurtu.registration.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {



    RecyclerView recyclerView;
//    ImageLoader imageLoader;
    List<Product> products;
    public Context context;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    ImageInternalFetcher fetcher ;

    public ProductsAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(context, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);
        fetcher = new ImageInternalFetcher(context, 150);
        fetcher.setLoadingImage(R.drawable.admin);
        fetcher.addImageCache(((ActivityHome)context).getSupportFragmentManager(),cacheParams);
//        this.imageLoader = DistributorApplication.getApplicationInstance().getImageLoader();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView productTitle;
        TextView productCategory;
        TextView productQuantity;
        TextView productCost, productId;
        ImageView productPreview, approvalView, rejectedView, waitingView ;
        public IMyViewHolderClicks mListener;
        public static interface IMyViewHolderClicks {
            public void onItemClick(View view, int position);
        }

        ProductViewHolder(View itemView, IMyViewHolderClicks iMyViewHolderClicks) {
            super(itemView);
            mListener = iMyViewHolderClicks;
            cv = (CardView)itemView.findViewById(R.id.cv);
            productTitle = (TextView)itemView.findViewById(R.id.product_title);
            productCategory = (TextView)itemView.findViewById(R.id.product_category);
            productQuantity = (TextView)itemView.findViewById(R.id.product_quantity);
            productCost     = (TextView)itemView.findViewById(R.id.product_cost);
            productPreview = (ImageView)itemView.findViewById(R.id.product_preview);
            productId = (TextView) itemView.findViewById(R.id.product_id);
            approvalView = (ImageView) itemView.findViewById(R.id.approved_icon);
            rejectedView = (ImageView) itemView.findViewById(R.id.rejected_icon);
            waitingView = (ImageView) itemView.findViewById(R.id.waiting_for_approval_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.onItemClick(v,getPosition());
            }
        }
    }
    public void removeProducts(){
        this.products.clear();
        notifyDataSetChanged();
    }
    public void addProducts(List<Product> productList){
        this.products.addAll(productList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return this.products!=null ? this.products.size() : 0;
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_product, viewGroup, false);

        //ProductViewHolder pvh = new ProductViewHolder(v);
        ProductViewHolder pvh = new ProductViewHolder(v, new ProductViewHolder.IMyViewHolderClicks() {
            @Override
            public void onItemClick(View view, int position) {

                FragmentManager fragmentManager = ((ActivityHome)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                if(products.get(position).getIsApproved() == 2){
                    Product prod = products.get(position);
                    AddNewProduct newProduct = AddNewProduct.newInstance(prod.getCatOneName(), prod.getCatTwoName(), prod.catThreeName, prod.catId+"", prod);
                    fragmentTransaction.replace(R.id.login_fragment,newProduct);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Log.v("position", "" + position);
                    bundle.putParcelable("product", products.get(position));
                    ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                    productDetailsFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.login_fragment, productDetailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return pvh;
    }
    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int position) {
        try {
            productViewHolder.productTitle.setText(products.get(position).prodName);
            productViewHolder.productCategory.setText(products.get(position).catOneName + ">" + products.get(position).catTwoName + ">" + products.get(position).catThreeName);
            productViewHolder.productQuantity.setText("Quantity:" + products.get(position).quantity);
            productViewHolder.productCost.setText("Rs." + products.get(position).price);
            productViewHolder.productId.setText("Product Id: " + products.get(position).getProdId());
//        Log.v("imageLoader", "" + imageLoader);
            String url = products.get(position).getImageURL().size() > 0 ? products.get(position).getImageURL().get(0) : "";
            url = url.substring(0, url.length()-5);
            url += "thumbnail.jpg";
            System.out.println("URL is " + url);
//            Uri uri = Uri.parse(url);
            fetcher.loadImage(url, productViewHolder.productPreview, 1);
            if(products.get(position).getIsApproved() == 0){
                productViewHolder.rejectedView.setVisibility(View.GONE);
                productViewHolder.waitingView.setVisibility(View.GONE);
                productViewHolder.approvalView.setVisibility(View.VISIBLE);
            }
            else if(products.get(position).getIsApproved() == 1){
                productViewHolder.rejectedView.setVisibility(View.GONE);
                productViewHolder.waitingView.setVisibility(View.VISIBLE);
                productViewHolder.approvalView.setVisibility(View.GONE);
            }
            else{
                productViewHolder.rejectedView.setVisibility(View.VISIBLE);
                productViewHolder.waitingView.setVisibility(View.GONE);
                productViewHolder.approvalView.setVisibility(View.GONE);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
//        imageLoader.DisplayImage(url, productViewHolder.productPreview);

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

}