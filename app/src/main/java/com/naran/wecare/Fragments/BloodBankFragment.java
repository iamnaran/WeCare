package com.naran.wecare.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naran.wecare.CustomViews.OrganizationAdapter;
import com.naran.wecare.Models.Organization;
import com.naran.wecare.Models.SharedPrefManager;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BloodBankFragment extends WeCareFragment {

    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeView;
    OrganizationAdapter organizationAdapter;
    List<Organization> organizationList;
    private Animation myAni;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_blood_bank, null);
        initiliseView(view);
        initialiseListener();
        getBloodBankData();
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
//        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager1);
        organizationList = new ArrayList<>();
        organizationAdapter = new OrganizationAdapter(getContext(), organizationList);
        recyclerView.setAdapter(organizationAdapter);


    }

    @Override
    protected void initiliseView(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewBloodBank);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        myAni = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);


        swipeView.setColorSchemeColors(

                Color.RED
        );

        FloatingActionButton editData = (FloatingActionButton) view.findViewById(R.id.editData);
        editData.setVisibility(View.INVISIBLE);

        if (SharedPrefManager.getInstance(getContext()).isOrgLoggedIn()) {

            editData.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (SharedPrefManager.getInstance(getContext()).isOrgLoggedIn()) {

                        showPostBloodBankData();

                    } else {
                        Toast.makeText(getContext(), "Sorry!! This feature is for Blood Bank Organization ", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }


        if (SharedPrefManager.getInstance(getContext()).isOrgLoggedIn()) {

            editData.setVisibility(View.VISIBLE);
            editData.startAnimation(myAni);


        }


    }


    @Override
    protected void initialiseListener() {

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                organizationList.clear();
                getBloodBankData();

            }
        });



    }

    private void getBloodBankData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstants.GET_BLOOD_BANK, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String username = jsonObject.getString(UrlConstants.KEY_ORG_USER_NAME);
                        String contact_number = jsonObject.getString(UrlConstants.KEY_ORG_CONTACT);

                        String aP = jsonObject.getString("aPositive");
                        String aN = jsonObject.getString("aNegative");
                        String bP = jsonObject.getString("bPositive");
                        String bN = jsonObject.getString("bNegative");
                        String abP = jsonObject.getString("abPositive");
                        String abN = jsonObject.getString("abNegative");
                        String oP = jsonObject.getString("oPositive");
                        String oN = jsonObject.getString("oNegative");

                        Organization org = new Organization();

                        org.setUserName(username);
                        org.setContactNumber(contact_number);
                        org.setaP(aP);
                        org.setaN(aN);
                        org.setbP(bP);
                        org.setbN(bN);
                        org.setAbP(abP);
                        org.setAbN(abN);
                        org.setoP(oP);
                        org.setoN(oN);

                        organizationList.add(org);

                    }
                    organizationAdapter.notifyDataSetChanged();
                    swipeView.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                swipeView.setRefreshing(false);


            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                try {
                    organizationList.clear();
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 1 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 4 * 60 * 60 * 1000; // in 4 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new String(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }

            }
            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

    }

    private void showPostBloodBankData() {

        final Dialog dialog = new Dialog(getActivity(), R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_blood_bank_data);

        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);
        ImageView done = (ImageView) dialog.findViewById(R.id.done);


        TextView orgName = (TextView) dialog.findViewById(R.id.org_full_name);
        final String org_name = SharedPrefManager.getInstance(getContext()).getOrgUsername();

        orgName.setText(org_name);

        final EditText aP = (EditText) dialog.findViewById(R.id.aP);
        final EditText aN = (EditText) dialog.findViewById(R.id.aN);
        final EditText bP = (EditText) dialog.findViewById(R.id.bP);
        final EditText bN = (EditText) dialog.findViewById(R.id.bN);
        final EditText oP = (EditText) dialog.findViewById(R.id.oP);
        final EditText oN = (EditText) dialog.findViewById(R.id.oN);
        final EditText abP = (EditText) dialog.findViewById(R.id.abP);
        final EditText abN = (EditText) dialog.findViewById(R.id.abN);




        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.POST_BLOOD_BANK, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "Oops ! Some error occur :( " + error, Toast.LENGTH_SHORT).show();


                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("org_name",org_name);
                        params.put("aPositive",aP.getText().toString());
                        params.put("aNegative",aN.getText().toString());
                        params.put("bPositive",bP.getText().toString());
                        params.put("bNegative",bN.getText().toString());
                        params.put("oPositive",oP.getText().toString());
                        params.put("oNegative",oN.getText().toString());
                        params.put("abPositive",abP.getText().toString());
                        params.put("abNegative",abN.getText().toString());

                        return params;
                    }
                };


                RequestQueue rqueue = Volley.newRequestQueue(getContext());
                rqueue.add(stringRequest);
            }
        });



        if (dialog.getWindow() != null)
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();



    }

}
