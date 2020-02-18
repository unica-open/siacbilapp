<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
  <body>                       
		<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
	  <div class="row-fluid">
	    <div class="span12 contentPage">    
			<form class="form-horizontal">
				<h3>Cassa Economale</h3>
	
				<div class="step-content">
					<div class="step-pane active" id="step1">
						<fieldset class="form-horizontal">
							<div class="span12">
							
							<div class="span9 offset1">
								<div class="boxStampe">
									<div class="titleCassa">Stampe</div>
									<div>
										<ul class="listSelectAccordion">
											<li>
												<a href="<s:property value='%{urlStampaGiornaleCassa}'/>">
													<i class="iconSmall icon-chevron-right"></i>giornale di cassa 
												</a>
											</li>
											<li >
												<a href="<s:property value='%{urlStampaRendiconto}'/>">
													<i class="iconSmall icon-chevron-right"></i>rendiconto
												</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
				
							
							
							</div>
							<div class="clear"></div>
						
						
						</fieldset>
					</div>
				</div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>      
	       
			</form>
	    </div>
			  
	  </div>	 
	</div>	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

</body>
</html>
