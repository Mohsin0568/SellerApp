package com.example.mohmurtu.registration.util;

/**
 * Created by mohmurtu on 11/8/2015.
 */
public class Constants {

    public static final int SOCKET_TIMEOUT = 90 * 1000;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;

    public static final String BASE_URL = "http://fantabazaar.com/eShopping";
    public static final String EMAIL_CHECK = "/seller/emailCheck" ;
    public static final String PHONE_CHECK = "/seller/phoneNumberCheck";
    public static final String REGISTER_URL = "/seller/register";
    public static final String LOGIN_URL = "/seller/login";
    public static final String CATEGORY_URL = "/categories/getAllCategories";
    public static final String CHECK_CATEGORY_URL = "/categories/checkAndGetAllCategories";
    public static final String GET_PROPERTIES_URL = "/property/getPropertiesAndValues";
    public static final String UPLOAD_PRODUCT_URL = "/products/addProductDetails";
    public static final String UPLOAD_IMAGE_URL = "/products/addProductImages";
    public static final String GET_PRODUCTS_URL = "/products/getProductsBySeller";
    public static final String UPDATE_PRODUCT = "/products/updateProductDetails";
    public static final String DASHBOARD_DATA = "/dashboard/getDashboardData";
    public static final String TOTAL_PRODUCTS = "/dashboard/totalProducts";
    public static final String PENDING_ORDERS = "/dashboard/pendingOrders";
    public static final String PRODUCTS_SOLD = "/dashboard/productsSold";
    public static final String AMOUNT_DUE = "/dashboard/amountDue";
    public static final String REVIEW = "/dashboard/review";
    public static final String COMMENTS_COUNT = "/dashboard/totalComments";
    public static final String GET_ACCOUNT_DETAILS = "/seller/getSellerAllDetails";
    public static final String CHANGE_PASSWORD = "/seller/changePassword";
    public static final String UPDATE_PERSONAL_DETAILS = "/seller/updateSellerPersonalDetails";
    public static final String UPDATE_ADDRESS_DETAILS = "/seller/updateSellerAddressDetails";
    public static final String UPDATE_BANK_DETAILS = "/seller/updateSellerBankDetails";
    public static final String UPDATE_VAT_DETAILS = "/seller/updateSellerVatDetails";
    public static final String REGISTER_GCM_ID = "/sellerGCM/register";
    public static final String GET_ORDERS = "/orders/getOrdersBySeller";
    public static final String CONFIRM_ORDER = "/orders/confirmOrderBySeller";
    public static final String CONFIRM_SHIP_ORDER = "/orders/confirmOrderDeliveryBySeller";


    public static final String REMEMBER_ME = "rememberMe";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String SELLER_ID = "sellerId";
    public static final String GCM_ID_AVAILABLE = "sellerGcmId";
    public static final String IS_GCM_ID_UPLOADED_IN_SERVER = "isGCMIdUploadedInServer";
    public static final String APPROVED_NOTIFICATION_MESSAGE = "approvedNotificationMessage";
    public static final String REJECTED_NOTIFICATION_MESSAGE = "rejectedNotificationMessage";

    public static final String CATONE = "categoryOne";
    public static final String CATTWO = "categoryTwo";
}
