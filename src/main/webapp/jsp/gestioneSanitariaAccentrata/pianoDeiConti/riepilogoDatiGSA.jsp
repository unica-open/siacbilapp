<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>


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
				<s:include value="/jsp/include/messaggi.jsp" />
				<h3>Piano dei Conti <s:property value="conto.pianoDeiConti.descrizione"/>- Classe <s:property value="conto.pianoDeiConti.classePiano.codice"/> </h3>
				
					<div class="step-pane active" id="step1">
	
						<fieldset class="form-horizontal">
					
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Dati Piano dei Conti</p>
									<ul class="htmlelt">
										<li>
											<dfn>Conto</dfn> 
											<dl><s:property value="conto.codice"/></dl>
										</li>
										<li>
											<dfn>Descrizione</dfn> 
											<dl><s:property value="conto.descrizione"/></dl>
										</li>
										<li>
											<dfn>Codifica interna</dfn> 
											<dl><s:property value="conto.codiceInterno"/></dl>
										</li>
										<li>
											<dfn>Tipo</dfn> 
											<dl><s:property value="conto.tipoConto.codice"/>-<s:property value="conto.tipoConto.descrizione"/></dl>
										</li>
										<li>
											<dfn>Conto di legge</dfn> 
											<dl>
												<s:if test="conto.contoDiLegge">S&iacute;</s:if><s:else>No</s:else>
											</dl>
										</li>
										<li>
											<dfn>Conto foglia</dfn> 
											<dl>
												<s:if test="conto.contoFoglia">S&iacute;</s:if><s:else>No</s:else>
											</dl>
										</li>
										
									</ul>
									
									<s:if test="conto.contoFoglia">
									<p>Dati aggiuntivi </p>
										<ul class="htmlelt">
											<li>
												<dfn>Codifica di bilancio</dfn> 
												<dl><s:property value="conto.codiceBilancio.descrizione"/></dl>
											</li>
											<li>
												<dfn>A Partite</dfn> 
												<dl>
												<s:if test="conto.contoAPartite">S&iacute;</s:if><s:else>No</s:else>
											</dl>
											</li>
											
											<li>
												<dfn>Tipo fondo</dfn> 
												<dl><s:property value="conto.tipoLegame.codice"/>-<s:property value="conto.tipoLegame.descrizione"/></dl>
											</li>
											<li>
												<dfn>Categoria Cespiti</dfn> 
												<dl><s:property value="conto.categoriaCespiti.codice"/>-<s:property value="conto.categoriaCespiti.descrizione"/></dl>
											</li>
											<li>
												<dfn>Collega Conto</dfn> 
												<dl><s:property value="conto.contoCollegato.codice"/>-<s:property value="conto.contoCollegato.descrizione"/></dl>
											</li>
										</ul>								
									</s:if>
											
								</div>
								
								<div class="boxOrInRight">
										
										<p>Soggetto</p>
											<ul class="htmlelt">
												<li>
													<dfn>Codice</dfn> 
													<dl><s:property value="conto.soggetto.codiceSoggetto"/></dl>
												</li>
												<li>
													<dfn>Codice Fiscale</dfn> 
													<dl><s:property value="conto.soggetto.codiceFiscale"/></dl>
												</li>
												<li>
													<dfn>Partita IVA</dfn> 
													<dl><s:property value="conto.soggetto.partitaIva"/></dl>
												</li>
												<li>
													<dfn>Denominazione</dfn> 
													<dl><s:property value="conto.soggetto.denominazione"/></dl>
												</li>
											</ul>	
									
								</div>
							</div>
						
						</fieldset>
					</div> 
					<p class="margin-medium">
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