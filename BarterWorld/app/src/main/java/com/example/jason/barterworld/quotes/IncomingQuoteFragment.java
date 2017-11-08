package com.example.jason.barterworld.quotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jason.barterworld.R;
import com.example.jason.barterworld.model.IncomingQuote;
import com.example.jason.barterworld.tools.RoundedTransformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**

 */
public class IncomingQuoteFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String user_uid="";
    public String display_uid="";

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;
    public String msg_key;

    private RecyclerView recyclerView;
    private DatabaseReference fire_db_incoming_quote_db;

   // private OnFragmentInteractionListener mListener;

    public IncomingQuoteFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
   /* public static IncomingQuoteFragment newInstance(String param1, String param2) {
        IncomingQuoteFragment fragment = new IncomingQuoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        fire_db_incoming_quote_db= FirebaseDatabase.getInstance().getReference().child("Incoming_Quotes").child(user_uid);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_incoming_quote, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView)view.findViewById(R.id.incoming_quote_recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     /*   if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
      //  mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();



        FirebaseRecyclerAdapter<IncomingQuote,IncomingQuoteFragment.IncomingQuoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<IncomingQuote,IncomingQuoteFragment.IncomingQuoteViewHolder>(
                IncomingQuote.class,
                R.layout.incoming_quote_row,
                IncomingQuoteFragment.IncomingQuoteViewHolder.class,
                fire_db_incoming_quote_db //change to   query_current_user_post

        ) {
            @Override
            protected void populateViewHolder(IncomingQuoteFragment.IncomingQuoteViewHolder viewHolder, IncomingQuote model, int position) {

                final String incoming_quote_key = getRef(position).getKey();//sender uid

                msg_key = model.getIncoming_msg();

                viewHolder.setBarterTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);
                viewHolder.setType(model.getType());
                viewHolder.setVal(model.getValue());
                viewHolder.setUserName(model.getUsername());


                viewHolder.setImg(getActivity().getApplicationContext(),model.getBarter_img());
                viewHolder.setUserImg(getActivity().getApplicationContext(),model.getSender_img());


                viewHolder.incoming_quote_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item

                        Intent Chat_Intent = new Intent(getActivity(), com.example.jason.barterworld.ChatActivity.class);

                        Chat_Intent.putExtra("msg_key",msg_key);

                        startActivity(Chat_Intent);



                    }
                });





            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    //------------------------------------------------------view_holder class-------------------------------

    public static class IncomingQuoteViewHolder extends RecyclerView.ViewHolder{

        FirebaseAuth user_auth;

        View incoming_quote_view;


        public IncomingQuoteViewHolder(View itemView) {
            super(itemView);
            incoming_quote_view=itemView;

            user_auth = FirebaseAuth.getInstance();

        }


        public void setBarterTitle(String title){

            TextView txt_title= (TextView)incoming_quote_view.findViewById(R.id.txt_post_name);
            txt_title.setText(title);
        }
        public void setUserName(String name){

            TextView txt_user_name= (TextView)incoming_quote_view.findViewById(R.id.txt_quote_user_name);
            txt_user_name.setText(name);
        }



        public void setDescription(String desc){

            TextView txt_desc= (TextView)incoming_quote_view.findViewById(R.id.txt_comment);
            txt_desc.setText(desc);

        }
        public void setType(String type){

            TextView txt_type= (TextView)incoming_quote_view.findViewById(R.id.txt_post_type);
            txt_type.setText(type);

        }
        public void setVal(String value){

            TextView txt_value= (TextView)incoming_quote_view.findViewById(R.id.txt_post_value);
            txt_value.setText("$value "+value);

        }




        public void setImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)incoming_quote_view.findViewById(R.id.img_post);
            //Picasso.with(ctx).load(img).into(post_img);

            Picasso.with(ctx).load(img).fit()
                    .placeholder(R.drawable.ic_media_play)
                    .centerCrop()
                    .transform(new RoundedTransformation(10, 4))
                    .into(post_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ctx).load(img).fit()
                                    .placeholder(R.drawable.ic_media_play).centerCrop()
                                    .transform(new RoundedTransformation(10, 4))
                                    .into(post_img);
                        }
                    });



        }

        public void setUserImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)incoming_quote_view.findViewById(R.id.user_img);
            //Picasso.with(ctx).load(img).into(post_img);

            Picasso.with(ctx).load(img).fit()
                    .placeholder(R.drawable.ic_media_play)
                    .centerCrop()
                    .transform(new RoundedTransformation(80, 4))
                    .into(post_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ctx).load(img).fit()
                                    .placeholder(R.drawable.ic_media_play).centerCrop()
                                    .transform(new RoundedTransformation(80, 4))
                                    .into(post_img);
                        }
                    });



        }







    }


    //-----------------------------------------------------view holder class--------------------------------

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
