package com.maeumteo.maeumteobackend.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Boolean checkid(String id){
        return !memberRepository.existsById(id);
    }

    public boolean saveMember(Member member) {
        Member saveMember = memberRepository.save(member);

        return saveMember != null;
    }

    public Optional<Member> loginMember(String id, String password) {
        return memberRepository.findByIdAndPassword(id,password);
    }

    public String getUserPassword(String id) {
        System.out.println(memberRepository.findById(id));
        return memberRepository.findById(id).get().getPassword();
    }

    public String formCheck(String key, String value){
        if(key.equals("nickname")){
            return memberRepository.existsByNickname(value) ? null : value;
        } else {
            return memberRepository.existsById(value) ? null : value;
        }
    }

    public Member socialLogin(Member member) {
        return memberRepository.findById(member.getId()).get();
    }

    public String kakaoLogin(String code) throws IOException {
        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String token = "";
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=클라이언트키값");
            sb.append("&redirect_uri=http://localhost:3000/kakaocallback");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result = " + result);

            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);

            token = access_token;

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return token;
    }

    public Map<String, Object> getUserInfo(String access_token) throws IOException {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> result = new HashMap<>();
        try {
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            System.out.println("res = " + res);

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject properties = (JSONObject) obj.get("properties");

            String email = kakao_account.get("email").toString();
            String nickname = properties.get("nickname").toString();
            String age_range = kakao_account.get("age_range").toString();

            result.put("email", email);
            result.put("nickname", nickname);
            result.put("age_range", age_range);

            br.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getAgreementInfo(String access_token)
    {
        String result = "";
        String host = "https://kapi.kakao.com/v2/user/scopes";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);
            // result is json format

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
