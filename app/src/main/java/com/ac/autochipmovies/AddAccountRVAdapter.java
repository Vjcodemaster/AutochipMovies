package com.ac.autochipmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Objects;

import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.SharedPreferencesClass;

public class AddAccountRVAdapter extends RecyclerView.Adapter<AddAccountRVAdapter.AddAccountViewHolder> {
    private RecyclerView recyclerView;
    private ArrayList<String> alAccountNames;
    private ArrayList<Integer> alAccountImages;

    private static final int UNSELECTED = -1;

    private int selectedItem = UNSELECTED;
    int position;
    private DatabaseHandler dbHandler;
    private boolean isSelected;
    private SharedPreferencesClass sharedPreferencesClass;
    private ArrayList<DataBaseHelper> alAccountsUserIDPassword = new ArrayList<>();

    AddAccountRVAdapter(RecyclerView recyclerView, ArrayList<String> alAccountNames, ArrayList<Integer> alAccountImages,
                        DatabaseHandler dbHandler, SharedPreferencesClass sharedPreferencesClass) {
        this.recyclerView = recyclerView;
        this.alAccountNames = alAccountNames;
        this.alAccountImages = alAccountImages;
        this.dbHandler = dbHandler;
        this.sharedPreferencesClass = sharedPreferencesClass;
        if (sharedPreferencesClass.getTotalAccountStatus())
            for (int i = 0; i < alAccountNames.size(); i++) {
                alAccountsUserIDPassword = new ArrayList<>(dbHandler.getIDPasswordOfAccountsUsingAccountName(alAccountNames.get(position)));
            }
    }

    @NonNull
    @Override
    public AddAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_add_account, parent, false);
        return new AddAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAccountViewHolder holder, int position) {

        holder.tvAccountName.setText(alAccountNames.get(position));
        //holder.tvAccountName.setCompoundDrawablesWithIntrinsicBounds(alAccountImages.get(position), 0, 0, 0);
        holder.ivAccountImage.setImageResource(alAccountImages.get(position));
        if(alAccountsUserIDPassword.size()>position) {
            holder.etUserName.getEditText().setText(alAccountsUserIDPassword.get(position).get_user_account_id());
            holder.etPassword.getEditText().setText(alAccountsUserIDPassword.get(position).get_user_account_password());
        }

        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return alAccountNames.size();
    }

    public class AddAccountViewHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener {
        //View.OnLongClickListener,
        TextView tvAccountName;
        ImageView ivAccountImage;
        ExpandableLayout elExpandedMenu;
        LinearLayout llExpandedMenuParent;
        TextInputLayout etUserName, etPassword;
        Button btnSave;

        AddAccountViewHolder(View itemView) {
            super(itemView);

            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            ivAccountImage = itemView.findViewById(R.id.iv_account_image);
            elExpandedMenu = itemView.findViewById(R.id.el_expanded_menu);

            etUserName = elExpandedMenu.findViewById(R.id.et_user_id);
            etPassword = elExpandedMenu.findViewById(R.id.et_password);
            btnSave = elExpandedMenu.findViewById(R.id.btn_save);

            llExpandedMenuParent = itemView.findViewById(R.id.ll_el_parent);

            elExpandedMenu.setInterpolator(new FastOutLinearInInterpolator());
            elExpandedMenu.setOnExpansionUpdateListener(this);

            //llExpandedMenuParent.setOnLongClickListener(this);
        }

        void bind(final AddAccountViewHolder holder, final int position) {
            //this.position = position;
            isSelected = position == selectedItem;
            llExpandedMenuParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SettingsActivity.onGenericInterfaceListener.onCommandReceived("DISPLAY_TOAST", position);
                    AddAccountViewHolder holder = (AddAccountViewHolder) recyclerView.findViewHolderForAdapterPosition(getAdapterPosition());
                    if (holder != null) {
                        if (selectedItem != -1 && selectedItem != position)
                            recyclerView.getAdapter().notifyItemChanged(selectedItem); //bit of hack to collapse previous expanded item
                        holder.llExpandedMenuParent.setSelected(false);
                        holder.elExpandedMenu.collapse();
                    }

                    int position = getAdapterPosition();
                    if (position == selectedItem) {
                        selectedItem = UNSELECTED;
                    } else {
                        llExpandedMenuParent.setSelected(true);
                        elExpandedMenu.expand();
                        selectedItem = position;
                    }
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sUserID = Objects.requireNonNull(etUserName.getEditText()).getText().toString().trim();
                    String sPassword = Objects.requireNonNull(etPassword.getEditText()).getText().toString().trim();
                    String sAccountName = tvAccountName.getText().toString();

                    if (validateFields(sUserID, sPassword)) {
                        int nID = dbHandler.getIdForAccountName(sAccountName);
                        dbHandler.updateIDAndPasswordToAccountsTable(new DataBaseHelper(sUserID, sPassword), nID);
                        SettingsActivity.onGenericInterfaceListener.onCommandReceived("GENERAL_TOAST", 3, "Information saved");
                        sharedPreferencesClass.setTotalAccountStatus(true);
                        //llExpandedMenuParent.setSelected(false);
                        elExpandedMenu.collapse();
                        selectedItem = -1;
                    }
                }
            });
            llExpandedMenuParent.setSelected(isSelected);
            elExpandedMenu.setExpanded(isSelected, false);
        }
        /*@Override
        public boolean onLongClick(View v) {
            AddAccountViewHolder holder = (AddAccountViewHolder) recyclerView.findViewHolderForAdapterPosition(getAdapterPosition());
            if (holder != null) {
                if (selectedItem != -1 && selectedItem != position)
                    recyclerView.getAdapter().notifyItemChanged(selectedItem); //bit of hack to collapse previous expanded item
                holder.llExpandedMenuParent.setSelected(false);
                holder.elExpandedMenu.collapse();
            }

            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                llExpandedMenuParent.setSelected(true);
                elExpandedMenu.expand();
                selectedItem = position;
            }
            //return true means that the event is consumed. It is handled. No other click events will be notified.
            //return false means the event is not consumed. Any other click events will continue to receive notifications.
            return true;
        }*/

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            if (state == ExpandableLayout.State.EXPANDING) {
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }

        private boolean validateFields(String sUserID, String sPassword) {
            String sResult;
            if (sUserID.length() < 3) {
                sResult = "Invalid User ID";
                //Toast.makeText(LoginActivity.this, "Invalid User ID", Toast.LENGTH_LONG).show();
                SettingsActivity.onGenericInterfaceListener.onCommandReceived("GENERAL_TOAST", 0, sResult);
                return false;
            }
            if (sPassword.length() < 5) {
                sResult = "Invalid Password!";
                //Toast.makeText(LoginActivity.this, "Invalid Password! Should be atleast of 5 characters", Toast.LENGTH_LONG).show();
                SettingsActivity.onGenericInterfaceListener.onCommandReceived("GENERAL_TOAST", 1, sResult);
                return false;
            }
            return true;
        }
    }
}
