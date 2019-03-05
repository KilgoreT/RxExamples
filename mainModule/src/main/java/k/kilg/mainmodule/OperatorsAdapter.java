package k.kilg.mainmodule;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import k.kilg.mainmodule.entity.RxOperator;

public class OperatorsAdapter extends RecyclerView.Adapter<OperatorsAdapter.OperatorsViewHolder>{

    private List<RxOperator> mData = new ArrayList<>();
    private OperatorAdapterCallback mListener;

    public OperatorsAdapter(Fragment fragment) {
        try {
            this.mListener = (OperatorAdapterCallback) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement OperatorAdapterCallback");
        }
    }

    public void setData(List<RxOperator> data) {
        this.mData = data;
    }

    @Override
    public OperatorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new OperatorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OperatorsViewHolder holder, int position) {
        RxOperator item = mData.get(position);
        holder.bind(item);
        holder.btnRun.setOnClickListener(v -> mListener.onRunClick(item.getClassName()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * ViewHolder
     */
    class OperatorsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.shortDoc)
        TextView shortDoc;

        @BindView(R.id.longDoc)
        TextView longDoc;

        @BindView(R.id.code)
        TextView code;

        @BindView(R.id.btn_run)
        ImageView btnRun;

        /*@BindView(R.id.gHeader)
        Guideline gHeader;*/


        OperatorsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            gHeader.setGuidelinePercent(.95f);
            longDoc.setVisibility(View.GONE);
            image.setImageResource(R.drawable.ic_expand_more);
            itemView.setOnClickListener(v -> {
                if (longDoc.getVisibility() == View.VISIBLE) {
//                    gHeader.setVisibility(View.GONE);
                    longDoc.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.ic_expand_more);
                } else {
//                    gHeader.setGuidelinePercent(.1f);
                    longDoc.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.ic_expand_less);
                }
            });
        }

        void bind(RxOperator rxOperator) {
            title.setText(rxOperator.getTitle());
            code.setText(rxOperator.getCode());
            longDoc.setText(rxOperator.getDocs());
        }

    }

    public interface OperatorAdapterCallback {
        void onRunClick(String operator);
    }
}
