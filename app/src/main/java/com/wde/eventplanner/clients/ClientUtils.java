package com.wde.eventplanner.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.wde.eventplanner.BuildConfig;
import com.wde.eventplanner.utils.TokenManager;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/v1/";

    private static OkHttpClient buildHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor authInterceptor = chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            String token = TokenManager.getToken(null);
            if (token != null)
                requestBuilder.header("Authorization", "Bearer " + token);
            return chain.proceed(requestBuilder.build());
        };

        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(authInterceptor).build();
    }

    private static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }

    private static class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalTime.parse(json.getAsString());
        }
    }

    private static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }

    public static final Gson gson = new GsonBuilder().setLenient()
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(buildHttpClient())
            .build();

    public static NotificationsService notificationsService = retrofit.create(NotificationsService.class);
    public static EventsService eventsService = retrofit.create(EventsService.class);
    public static ListingsService listingsService = retrofit.create(ListingsService.class);
    public static ListingCategoriesService listingCategoriesService = retrofit.create(ListingCategoriesService.class);
    public static EventTypesService eventTypesService = retrofit.create(EventTypesService.class);
    public static InvitationService invitationService = retrofit.create(InvitationService.class);
    public static ProductsService productsService = retrofit.create(ProductsService.class);
    public static ServicesService servicesService = retrofit.create(ServicesService.class);
    public static ProductBudgetItemService productBudgetItemService = retrofit.create(ProductBudgetItemService.class);
    public static ServiceBudgetItemService serviceBudgetItemService = retrofit.create(ServiceBudgetItemService.class);
    public static UsersService usersService = retrofit.create(UsersService.class);
    public static ListingReviewsService listingReviewsService = retrofit.create(ListingReviewsService.class);
    public static EventReviewsService eventReviewsService = retrofit.create(EventReviewsService.class);
    public static GuestService guestService = retrofit.create(GuestService.class);
    public static EventOrganizerService eventOrganizerService = retrofit.create(EventOrganizerService.class);
    public static SellerService sellerService = retrofit.create(SellerService.class);
    public static ChatsService chatsService = retrofit.create(ChatsService.class);
    public static UserReportsService userReportsService = retrofit.create(UserReportsService.class);
}

