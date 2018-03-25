package json.retrofit.jason.json_webservice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 11/8/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.MyViewHolder>{

    public List<User> user;

    public RecyclerAdapter( List<User> _User){this.user=_User;}


    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {

        holder.txt_username.setText(user.get(position).getUser_name());
        holder.txt_useremail.setText(user.get(position).getUser_email());
        holder.txt_userpwd.setText(user.get(position).getUser_pwd());

    }

    @Override
    public int getItemCount() {
        return user.size();
    }


    public  class MyViewHolder extends RecyclerView.ViewHolder{


        TextView txt_username, txt_useremail,txt_userpwd;



        public MyViewHolder(View itemView) {
            super(itemView);
            txt_username=(TextView)itemView.findViewById(R.id.txt_username);
            txt_useremail=(TextView)itemView.findViewById(R.id.txt_useremail);
            txt_userpwd=(TextView)itemView.findViewById(R.id.txt_userpwd);

        }
    }
}
