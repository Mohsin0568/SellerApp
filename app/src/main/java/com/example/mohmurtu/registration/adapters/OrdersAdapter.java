package com.example.mohmurtu.registration.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohmurtu.registration.ActivityHome;
import com.example.mohmurtu.registration.OrderDetailFragment;
import com.example.mohmurtu.registration.R;
import com.example.mohmurtu.registration.imagesUtil.ImageCache;
import com.example.mohmurtu.registration.imagesUtil.ImageInternalFetcher;
import com.example.mohmurtu.registration.model.orders.Order;

import java.util.List;

/**
 * Created by mohmurtu on 2/20/2016.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    RecyclerView recyclerView ;
    List<Order> orders;
    public Context context;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    ImageInternalFetcher fetcher ;

    public OrdersAdapter(Context context, List<Order> orders){
        this.context = context;
        this.orders = orders;
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(context, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);
        fetcher = new ImageInternalFetcher(context, 150);
        fetcher.setLoadingImage(R.drawable.admin);
        fetcher.addImageCache(((ActivityHome)context).getSupportFragmentManager(), cacheParams);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView orderCardView ;
        TextView productTitle, productQuantity, productPrice, orderStatus, orderCode;
        ImageView productImage ;
        OrderViewHolderClicks listener ;
        public static interface OrderViewHolderClicks {
            public void onItemClick(View view, int position);
        }

        public OrderViewHolder(View itemView, OrderViewHolderClicks iMyViewHolderClicks){
            super(itemView);
            listener = iMyViewHolderClicks;
            orderCardView = (CardView)  itemView.findViewById(R.id.orders_card_view);
            productTitle = (TextView) itemView.findViewById(R.id.orders_product_title);
            productQuantity = (TextView) itemView.findViewById(R.id.orders_product_quantity);
            productPrice = (TextView) itemView.findViewById(R.id.orders_product_cost);
            orderStatus = (TextView) itemView.findViewById(R.id.order_status);
            orderCode = (TextView) itemView.findViewById(R.id.order_code);
            productImage = (ImageView) itemView.findViewById(R.id.order_image_preview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.onItemClick(v,getPosition());
            }
        }
    }

    public void removeOrders(){
        this.orders.clear();
        notifyDataSetChanged();
    }
    public void addOrders(List<Order> ordersList){
        this.orders.addAll(ordersList);
        notifyDataSetChanged();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_row, viewGroup, false);
        OrderViewHolder ovh = new OrderViewHolder(v, new OrderViewHolder.OrderViewHolderClicks(){
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fragmentManager = ((ActivityHome)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OrderDetailFragment odf = new OrderDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("order", orders.get(position));
                bundle.putParcelable("address", orders.get(position).getAddress());
                bundle.putParcelable("product", orders.get(position).getProduct());
                odf.setArguments(bundle);
                fragmentTransaction.replace(R.id.login_fragment,odf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return ovh;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.productTitle.setText(orders.get(position).getProduct().getProdName());
        holder.productQuantity.setText("Quantity Ordered: " + orders.get(position).getOrderedQuantity());
        holder.productPrice.setText("Total price: " + orders.get(position).getTotalCost());
        holder.orderStatus.setText(orders.get(position).getStatusMessage());
        holder.orderCode.setText(orders.get(position).getOrderSellerCode());
        String url = orders.get(position).getProduct().getCoverImagePath();
        System.out.println("URL is " + url);
//            Uri uri = Uri.parse(url);
        fetcher.loadImage(url, holder.productImage, 1);
    }

    @Override
    public int getItemCount() {
        return this.orders!=null ? this.orders.size() : 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }
}
