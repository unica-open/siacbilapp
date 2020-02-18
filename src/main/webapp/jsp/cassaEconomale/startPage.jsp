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
				<s:hidden name="necessarioSelezionareCassa" id="necessarioSelezionareCassa" />
				<s:form cssClass="form-horizontal" action="#">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>
						Cassa Economale: <s:if test="%{listaCassaEconomale.size()>0}"><a id="changeCassaEconomale"><span class="datiCassaEconomale">
							<s:property value="cassaEconomale.descrizione" />&nbsp;<i class="marginLeft1 icon-pencil icon-08x"></i>
						</span></a></s:if>
						<s:if test="%{!listaCassaEconomale.size()>0}"><span class="datiCassaEconomale">
							<s:property value="cassaEconomale.descrizione" />
						</span></s:if>
					</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<div class="span12 containerCEC">
									<div class="colType span9">
										<div class="containerTipologie">
											<div class="divTipologie">tipologie</div>
											<div class="lineType"></div>
										</div>
										<div class="containerElenco">
											<ul class="listManagement">
												<s:iterator value="listaTipologiaRichiestaCassaEconomale" var="tipologiaRichiesta">
													<li class="bulletCircle">
														<div class="linkPonteir"><s:property value="%{#tipologiaRichiesta.tipoRichiestaEconomale.descrizione}"/></div>
														<ul class="ListSecond">
															<s:iterator value="%{#tipologiaRichiesta.listaAbilitazioneRichiestaCassaEconomale}" var="abilitazione">
																<li class="bulletCircleSet">
																	<a class="linkSecVoice<s:if test="%{!#abilitazione.abilitato}"> disableCas</s:if>" href="<s:property value="%{#abilitazione.url}" />">
																		<s:property value="%{#abilitazione.nome}" /><i class="icon-chevron-right trailing"></i>
																	</a>
																</li>
															</s:iterator>
														</ul>
												</s:iterator>
											</ul>
										</div>
									</div>
									<div class="colExtra span3">
										<div class="divExtra">
											<s:iterator value="listaOperazioniExtra" var="operazione">
												<div class="boxOperCassa">
													<div class="titleCassa"><s:property value="%{#operazione.tipoRichiestaEconomale.descrizione}" /></div>
													<ul class="listSelectAccordion">
														<s:iterator value="%{#operazione.listaAbilitazioneRichiestaCassaEconomale}" var="abilitazioneOp">
															<li <s:if test="%{!#abilitazioneOp.abilitato}">class="disabCs"</s:if>>
																<a href="<s:property value="%{#abilitazioneOp.url}" />">
																	<i class="iconSmall icon-chevron-right"></i><s:property value="%{#abilitazioneOp.nome}" />
																</a>
															</li>
														</s:iterator>
													</ul>
												</div>
											</s:iterator>
										</div>
									</div>
								</div>
								<div class="clear"></div>
								<br/>
								<br/>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:include value="/jsp/cassaEconomale/modaleSelezioneCassaEconomale.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cassaEconomale/start.js"></script>
	
</body>
</html>