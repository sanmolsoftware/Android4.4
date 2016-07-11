
package com.ulta.core.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.ShopListActivity;

public class QnAThankyouActivity extends UltaBaseActivity
{
	ImageButton btnShopMore;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qna_thankyou_layout);
		setTitle("Thank You");
		btnShopMore=(ImageButton)findViewById(R.id.btnShopMore);
		btnShopMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentForShop = new Intent(
						QnAThankyouActivity.this, ShopListActivity.class);
				intentForShop.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentForShop);
				finish();
			}
		});
		/*btnGoBack=(Button)findViewById(R.id.btnGoBackToProduct);
		btnGoBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(QnAThankyouActivity.this, UltaProductDetailsActivity.class).putExtra("id", id));
				finish();
			}
		});*/
	}
}
