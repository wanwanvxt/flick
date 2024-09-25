package vn.edu.eaut.flick.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.helpers.NetworkMonitor;
import vn.edu.eaut.flick.views.fragments.BookmarksFragment;
import vn.edu.eaut.flick.views.fragments.HomeFragment;
import vn.edu.eaut.flick.views.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
  private BottomNavigationView bottomNavigationView;
  //
  private HomeFragment homeFragment;
  private SearchFragment searchFragment;
  private BookmarksFragment bookmarksFragment;
  //
  private NetworkMonitor networkMonitor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    initialize();
    showFragment(homeFragment);
  }

  @Override
  protected void onStart() {
    super.onStart();
    networkMonitor.registerNetworkCallback();
  }

  @Override
  protected void onStop() {
    super.onStop();
    networkMonitor.unregisterNetworkCallback();
  }

  private void initialize() {
    bottomNavigationView = findViewById(R.id.bottomNav);
    bottomNavigationView.setOnItemSelectedListener(this::bottomNavItemSelectedListener);
    //
    homeFragment = new HomeFragment();
    searchFragment = new SearchFragment();
    bookmarksFragment = new BookmarksFragment();
    //
    networkMonitor = new NetworkMonitor(this);
  }

  private boolean bottomNavItemSelectedListener(MenuItem item) {
    if (item.getItemId() == R.id.nav_home) {
      showFragment(homeFragment);
      return true;
    }
    if (item.getItemId() == R.id.nav_search) {
      showFragment(searchFragment);
      return true;
    }
    if (item.getItemId() == R.id.nav_bookmarks) {
      showFragment(bookmarksFragment);
      return true;
    }
    return false;
  }

  private void showFragment(Fragment fragment) {
    @SuppressLint("CommitTransaction") FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    if (homeFragment.isAdded()) transaction.hide(homeFragment);
    if (searchFragment.isAdded()) transaction.hide(searchFragment);
    if (bookmarksFragment.isAdded()) transaction.hide(bookmarksFragment);

    if (fragment.isAdded()) {
      transaction.show(fragment);
    } else {
      transaction.add(R.id.frameLayout, fragment);
    }

    transaction.commit();
  }
}
